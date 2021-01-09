package io.github.mvillafuertem.chat.configuration

import akka.http.scaladsl.model
import akka.http.scaladsl.model.MediaTypes.`text/html`
import akka.http.scaladsl.model.{HttpCharsets, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{encodeResponse, extractLog, extractUnmatchedPath, getFromDirectory, getFromFile, getFromResource, getFromResourceDirectory, pathEndOrSingleSlash, withRequestTimeoutResponse}
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.server.RouteConcatenation._
import io.github.mvillafuertem.chat.api.MessageEndpoint.getAllMessages
import io.github.mvillafuertem.chat.api.{AuthController, UserController}
import io.github.mvillafuertem.chat.application.AuthenticateUser.ZAuthenticateUser
import io.github.mvillafuertem.chat.application.CreateNewUser.ZCreateNewUser
import io.github.mvillafuertem.chat.domain.model.Message
import sttp.tapir.server.akkahttp._
import zio._

import scala.concurrent.Future

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
                extractLog { log =>
                pathEndOrSingleSlash {
                  getFromResource("assets/index.html", model.ContentType(`text/html`, HttpCharsets.`UTF-8`))
                } ~
                  getFromResourceDirectory("assets") ~
                  userRoutes ~
                  authRoutes ~
                  //renewTokenRoute ~
                  getAllMessagesRoute ~
                  Directives.get {
                    extractUnmatchedPath { path =>
                    log.info(s"EXTRACT UNMATCHED PATH $path")
                        getFromResource("assets/index.html", model.ContentType(`text/html`, HttpCharsets.`UTF-8`))
                    }
                  }
                }

              val routes: Route = assets

              routes
            }
          }
        }

    } yield routes).toLayer

}
