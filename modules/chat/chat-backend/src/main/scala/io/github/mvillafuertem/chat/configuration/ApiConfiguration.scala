package io.github.mvillafuertem.chat.configuration

import akka.http.scaladsl.model
import akka.http.scaladsl.model.MediaTypes.`text/html`
import akka.http.scaladsl.model.{ HttpCharsets, HttpResponse, StatusCodes }
import akka.http.scaladsl.server.Directives.{ getFromResource, getFromResourceDirectory, pathEndOrSingleSlash, withRequestTimeoutResponse }
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.RouteConcatenation._
import io.github.mvillafuertem.chat.api.AuthEndpoint.renewToken
import io.github.mvillafuertem.chat.api.MessageEndpoint.getAllMessages
import io.github.mvillafuertem.chat.api.{ AuthController, UserController }
import io.github.mvillafuertem.chat.application.AuthenticateUser.ZAuthenticateUser
import io.github.mvillafuertem.chat.application.CreateNewUser.ZCreateNewUser
import io.github.mvillafuertem.chat.domain.model.{ Message, User }
import pdi.jwt.{ JwtAlgorithm, JwtCirce, JwtClaim }
import sttp.tapir.server.akkahttp._
import zio._

import java.time.Instant
import java.time.temporal.ChronoUnit
import scala.concurrent.Future
import scala.util.{ Failure, Success }

trait ApiConfiguration {

  val routes: Route

}

object ApiConfiguration {

  type ZApiConfiguration = Has[ApiConfiguration]

  val live: ZLayer[ZAuthenticateUser with ZCreateNewUser, Throwable, ZApiConfiguration] =
    (for {
      authRoutes <- AuthController.routes
      userRoutes <- UserController.routes
      routes <- ZManaged
        .runtime[ZCreateNewUser]
        .map { implicit runtime: zio.Runtime[ZCreateNewUser] =>
          new ApiConfiguration {
            override val routes: Route = withRequestTimeoutResponse(request =>
              HttpResponse(StatusCodes.EnhanceYourCalm, entity = "Unable to serve response within time limit, please enhance your calm.")
            ) {

              val getAllMessagesRoute: Route = getAllMessages.toRoute(_ => Future(Right(Seq(Message("", "", ""))))(runtime.platform.executor.asEC))

              def assets: Route =
                pathEndOrSingleSlash {
                  getFromResource("assets/index.html", model.ContentType(`text/html`, HttpCharsets.`UTF-8`))
                } ~
                  getFromResourceDirectory("assets") ~
                  userRoutes ~
                  authRoutes ~
                  //renewTokenRoute ~
                  getAllMessagesRoute

              val routes: Route = assets

              routes
            }
          }
        }

    } yield routes).toLayer

}
