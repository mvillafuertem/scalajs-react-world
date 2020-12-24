package io.github.mvillafuertem.chat.api

import akka.http.scaladsl.server.Route
import io.github.mvillafuertem.chat.api.AuthEndpoint.newUser
import io.github.mvillafuertem.chat.application.CreateNewUser
import io.github.mvillafuertem.chat.infrastructure.MongoUserRepository.ZUserRepository
import sttp.tapir.server.akkahttp._
import zio.{ZEnv, ZManaged}

trait AuthController {




  //ZManaged.runtime[ZUserRepository].map(_.unsafeRunToFuture(CreateNewUser.createUser(user).runHead))

  //val newUserRoute: Route = newUser.toRoute(user => runtime.unsafeRunToFuture(CreateNewUser.createUser(user).runHead.)(runtime.platform.executor.asEC))



}

object AuthController extends AuthController
