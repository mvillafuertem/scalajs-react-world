package io.github.mvillafuertem.chat.api

import akka.NotUsed
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Source
import akka.util.ByteString
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import io.github.mvillafuertem.chat.application.CreateNewUser
import io.github.mvillafuertem.chat.application.CreateNewUser.ZCreateNewUser
import io.github.mvillafuertem.chat.domain.error.ChatError
import io.github.mvillafuertem.chat.domain.model.User
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
import zio.ZManaged
import zio.stream.Sink

trait UserController {

  val routes: ZManaged[ZCreateNewUser, Nothing, Route] = ZManaged
    .runtime[ZCreateNewUser]
    .map { implicit runtime: zio.Runtime[ZCreateNewUser] =>
      val createNewUserRoute = AkkaHttpServerInterpreter.toRoute[User, ChatError, Source[ByteString, Any]](
        AuthEndpoint.newUser
      )(user =>
        runtime.unsafeRunToFuture(
          CreateNewUser
            .createUser(user)
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
                      d <- users
                    } yield source.concat(d)
                }
            )
        )
      )
      createNewUserRoute
    }

}

object UserController extends UserController
