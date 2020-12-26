package io.github.mvillafuertem.chat.api

import akka.stream.scaladsl.Source
import akka.util.ByteString
import io.circe.generic.auto._
import io.circe.syntax._
import io.github.mvillafuertem.chat.application.CreateNewUser
import io.github.mvillafuertem.chat.application.CreateNewUser.ZCreateNewUser
import io.github.mvillafuertem.chat.configuration.InfrastructureConfiguration
import io.github.mvillafuertem.chat.infrastructure.MongoUserRepository
import io.github.mvillafuertem.chat.model.error.ChatError
import io.github.mvillafuertem.shared.User
import org.reactivestreams.Publisher
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
import zio.{CanFail, ZIO}
import zio.interop.reactivestreams._
import zio.stream.ZStream

trait AuthController extends InfrastructureConfiguration {

//  AkkaHttpServerInterpreter.toRoute[User, String, Source[ByteString, Any]](
//    AuthEndpoint.newUser
//  ) { user =>
//    zio.Runtime
//      .unsafeFromLayer(mongoDBLayer >>> MongoUserRepository.live >>> CreateNewUser.live)
//      .unsafeRunToFuture(
//        CreateNewUser
//          .createUser(user)
//          .map(bytes => akka.util.ByteString(bytes.asJson.noSpaces))
//          .either
//          .toPublisher
//          .map{a =>
//            Source.fromPublisher(a).map(_.fold(a => ByteString(a.asJson.noSpaces), identity))
//          }
//          .either(CanFail.canFail[Nothing])
//      )
//  }

  //val newUserRoute: Route = newUser.toRoute(user => runtime.unsafeRunToFuture(CreateNewUser.createUser(user).runHead.)(runtime.platform.executor.asEC))

}

object AuthController extends AuthController
