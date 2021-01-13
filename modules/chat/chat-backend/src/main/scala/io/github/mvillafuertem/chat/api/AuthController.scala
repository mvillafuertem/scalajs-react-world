package io.github.mvillafuertem.chat.api

import akka.NotUsed
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.RouteConcatenation._
import akka.stream.scaladsl.Source
import akka.util.ByteString
import io.circe.generic.auto._
import io.circe.syntax._
import io.github.mvillafuertem.chat.application.AuthenticateUser
import io.github.mvillafuertem.chat.application.AuthenticateUser.ZAuthenticateUser
import io.github.mvillafuertem.chat.configuration.InfrastructureConfiguration
import io.github.mvillafuertem.chat.domain.error.ChatError
import io.github.mvillafuertem.chat.domain.model.User
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
import zio.ZManaged
import zio.stream.Sink

trait AuthController extends InfrastructureConfiguration {

  val routes: ZManaged[ZAuthenticateUser, Nothing, Route] = ZManaged
    .runtime[ZAuthenticateUser]
    .map { implicit runtime: zio.Runtime[ZAuthenticateUser] =>
      val authenticateUserRoute = AkkaHttpServerInterpreter.toRoute[User, ChatError, Source[ByteString, Any]](
        AuthEndpoint.loginUser
      )(user =>
        runtime.unsafeRunToFuture(
          AuthenticateUser
            .authenticateUser(user)
            .map(_.asJson.noSpaces)
            .map(ByteString(_))
            .map(Source.single)
            .either
            .run(
              Sink
                .foldLeft[Either[ChatError, Source[ByteString, NotUsed]], Either[ChatError, Source[ByteString, NotUsed]]](Right(Source.empty)) {
                  case (input, users) =>
                    for {
                      source <- input
                      d      <- users
                    } yield source.concat(d)
                }
            )
        )
      )

      val authenticateRenewRoute = AkkaHttpServerInterpreter.toRoute[AuthToken, ChatError, Source[ByteString, Any]](
        AuthEndpoint.renewToken
      )(token =>
        runtime.unsafeRunToFuture(
          AuthenticateUser
            .renewToken(token)
            .map(_.asJson.noSpaces)
            .map(ByteString(_))
            .map(Source.single)
            .either
            .run(
              Sink
                .foldLeft[Either[ChatError, Source[ByteString, NotUsed]], Either[ChatError, Source[ByteString, NotUsed]]](Right(Source.empty)) {
                  case (input, users) =>
                    for {
                      source <- input
                      d      <- users
                    } yield source.concat(d)
                }
            )
        )
      )
      authenticateUserRoute ~ authenticateRenewRoute
    }
}

object AuthController extends AuthController
