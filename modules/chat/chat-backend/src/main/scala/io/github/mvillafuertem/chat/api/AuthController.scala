package io.github.mvillafuertem.chat.api

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.RouteConcatenation._
import akka.stream.scaladsl.Source
import akka.util.ByteString
import io.github.mvillafuertem.chat.application.AuthenticateUser
import io.github.mvillafuertem.chat.application.AuthenticateUser.ZAuthenticateUser
import io.github.mvillafuertem.chat.configuration.InfrastructureConfiguration
import io.github.mvillafuertem.chat.domain.error.ChatError
import io.github.mvillafuertem.chat.domain.model.{ Jwt, User }
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
import zio.ZManaged
import io.circe.generic.auto._
import io.circe.syntax._

trait AuthController extends InfrastructureConfiguration {

  val routes: ZManaged[ZAuthenticateUser, Nothing, Route] = ZManaged
    .runtime[ZAuthenticateUser]
    .map { implicit runtime: zio.Runtime[ZAuthenticateUser] =>
      val authenticateUserRoute = AkkaHttpServerInterpreter.toRoute[User, ChatError, Source[ByteString, Any]](AuthEndpoint.loginUser)(user =>
        runtime.unsafeRunToFuture(
          UnsafeRunZStreams.run[ZAuthenticateUser, ChatError](AuthenticateUser.authenticateUser(user).map(_.asJson.noSpaces))(UnsafeRunZStreams.zsinkHeadEither)
        )
      )
      val authenticateRenewRoute = AkkaHttpServerInterpreter.toRoute[AuthToken, ChatError, Source[ByteString, Any]](AuthEndpoint.renewToken)(token =>
        runtime.unsafeRunToFuture(
          UnsafeRunZStreams.run[ZAuthenticateUser, ChatError](AuthenticateUser.renewToken(token).map(_.asJson.noSpaces))(UnsafeRunZStreams.zsinkEither)
        )
      )
      authenticateUserRoute ~ authenticateRenewRoute
    }
}

object AuthController extends AuthController
