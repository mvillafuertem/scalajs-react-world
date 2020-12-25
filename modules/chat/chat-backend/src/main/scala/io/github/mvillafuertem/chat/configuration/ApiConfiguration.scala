package io.github.mvillafuertem.chat.configuration

import akka.http.scaladsl.model.HttpCharsets.`UTF-8`
import akka.http.scaladsl.model.MediaTypes.`text/html`
import akka.http.scaladsl.model.{ ContentType, HttpResponse, StatusCodes }
import akka.http.scaladsl.server.Directives.{ getFromResource, getFromResourceDirectory, pathEndOrSingleSlash, withRequestTimeoutResponse }
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.RouteConcatenation._
import akka.stream.scaladsl.Source
import akka.util.ByteString
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import io.github.mvillafuertem.chat.api.AuthEndpoint
import io.github.mvillafuertem.chat.api.AuthEndpoint.{ loginUser, newUser, renewToken }
import io.github.mvillafuertem.chat.api.MessageEndpoint.getAllMessages
import io.github.mvillafuertem.chat.application.CreateNewUser
import io.github.mvillafuertem.chat.application.CreateNewUser.ZCreateNewUser
import io.github.mvillafuertem.shared.{ Message, User }
import pdi.jwt.{ JwtAlgorithm, JwtCirce, JwtClaim }
import sttp.tapir.server.akkahttp._
import zio.interop.reactivestreams._
import zio.{ CanFail, Has, ZLayer, ZManaged }

import java.time.Instant
import java.time.temporal.ChronoUnit
import scala.concurrent.Future
import scala.util.{ Failure, Success }

trait ApiConfiguration {

  val routes: Route

}

object ApiConfiguration {

  type ZApiConfiguration = Has[ApiConfiguration]

  val live: ZLayer[ZCreateNewUser, Throwable, ZApiConfiguration] =
    ZManaged
      .runtime[ZCreateNewUser]
      .map { implicit runtime: zio.Runtime[ZCreateNewUser] =>
        new ApiConfiguration {
          override val routes: Route = withRequestTimeoutResponse(request =>
            HttpResponse(StatusCodes.EnhanceYourCalm, entity = "Unable to serve response within time limit, please enhance your calm.")
          ) {
            //val newUserRoute: Route = newUser.toRoute(user => runtime.unsafeRunToFuture(CreateNewUser.createUser(user).runHead.map(_.toRight[String]("error"))))
            val newUserRoute: Route = AkkaHttpServerInterpreter.toRoute[User, String, Source[ByteString, Any]](
              AuthEndpoint.newUser
            ) { user =>
              runtime.unsafeRunToFuture(
                CreateNewUser
                  .createUser(user)
                  .map(bytes => akka.util.ByteString(bytes.asJson.noSpaces))
                  .toPublisher
                  .map(Source.fromPublisher)
                  .either(CanFail.canFail[Nothing])
              )
            }

            val loginUserRoute: Route = loginUser.toRoute { _ =>
              Future(Right(User("", "", "")))(runtime.platform.executor.asEC)
            }
            val renewTokenRoute: Route = renewToken.toRoute(_ =>
              Future(Right {

                //val tk = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MDgyMjkxMjMsImlhdCI6MTYwODIyMTkyM30.1bCYFnYR-o0ILIFuRJ2wxJ4UhFYKB42KuZmEhAjisTI"
                val now = Instant.now
                val claim = JwtClaim(
                  content = """{"name": "asdfasd"}""",
                  expiration = Some(now.plus(2, ChronoUnit.HOURS).getEpochSecond),
                  issuedAt = Some(now.getEpochSecond)
                )
                val key       = "secretKey"
                val algo      = JwtAlgorithm.HS256
                val token     = JwtCirce.encode(claim, key, algo)
                val triedJson = JwtCirce.decodeJson(token, key, Seq(JwtAlgorithm.HS256))
                triedJson match {
                  case Failure(exception) => throw exception
                  case Success(value) =>
                    User(token, "", value.noSpaces)
                }
              })(runtime.platform.executor.asEC)
            )
            val getAllMessagesRoute: Route = getAllMessages.toRoute(_ => Future(Right(Seq(Message("", "", ""))))(runtime.platform.executor.asEC))

            def assets: Route =
              pathEndOrSingleSlash {
                getFromResource("assets/index.html", ContentType(`text/html`, `UTF-8`))
              } ~
                getFromResourceDirectory("assets") ~
                newUserRoute ~
                loginUserRoute ~
                renewTokenRoute ~
                getAllMessagesRoute

            val routes: Route = assets

            routes
          }
        }
      }
      .toLayer

}
