package io.github.mvillafuertem.chat.api

import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Source
import akka.util.ByteString
import io.github.mvillafuertem.chat.application.CreateNewUser
import io.github.mvillafuertem.chat.application.CreateNewUser.ZCreateNewUser
import io.github.mvillafuertem.chat.domain.error.ChatError
import io.github.mvillafuertem.chat.domain.model.User
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
import zio.ZManaged
import io.circe.generic.auto._
import io.circe.syntax._
trait UserController {

  val routes: ZManaged[ZCreateNewUser, Nothing, Route] = ZManaged
    .runtime[ZCreateNewUser]
    .map { implicit runtime: zio.Runtime[ZCreateNewUser] =>
      val createNewUserRoute = AkkaHttpServerInterpreter.toRoute[User, ChatError, Source[ByteString, Any]](AuthEndpoint.newUser)(user =>
        runtime.unsafeRunToFuture(UnsafeRunZStreams.run[ZCreateNewUser, ChatError](CreateNewUser.createUser(user).map(_.asJson.noSpaces))(UnsafeRunZStreams.zsinkEither))
      )
      createNewUserRoute
    }

}

object UserController extends UserController
