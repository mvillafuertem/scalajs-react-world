package io.github.mvillafuertem.chat.api

import akka.stream.scaladsl.Source
import akka.util.ByteString
import io.circe.generic.auto._
import io.circe.syntax._
import io.github.mvillafuertem.chat.application.CreateNewUser
import io.github.mvillafuertem.chat.configuration.InfrastructureConfiguration
import io.github.mvillafuertem.chat.infrastructure.MongoUserRepository
import io.github.mvillafuertem.shared.User
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
import zio.CanFail
import zio.interop.reactivestreams._

trait AuthController extends InfrastructureConfiguration {

  AkkaHttpServerInterpreter.toRoute[User, String, Source[ByteString, Any]](
    AuthEndpoint.newUser
  ) { user =>
    zio.Runtime
      .unsafeFromLayer(mongoDBLayer >>> MongoUserRepository.live >>> CreateNewUser.live)
      .unsafeRunToFuture(
        CreateNewUser
          .createUser(user)
          .map(bytes => akka.util.ByteString(bytes.asJson.noSpaces))
          .toPublisher
          .map(Source.fromPublisher)
          .either(CanFail.canFail[Nothing])
      )
  }

  //val newUserRoute: Route = newUser.toRoute(user => runtime.unsafeRunToFuture(CreateNewUser.createUser(user).runHead.)(runtime.platform.executor.asEC))

}

object AuthController extends AuthController
