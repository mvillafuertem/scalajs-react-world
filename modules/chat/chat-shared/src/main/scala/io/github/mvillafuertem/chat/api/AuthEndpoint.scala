package io.github.mvillafuertem.chat.api

import akka.stream.scaladsl.Source
import akka.util.ByteString
import io.circe.generic.auto._
import io.github.mvillafuertem.shared.User
import sttp.capabilities.akka.AkkaStreams
import sttp.capabilities.zio.ZioStreams
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.model.UsernamePassword
import zio.stream

trait AuthEndpoint {

  val loginUser: Endpoint[Unit, String, User, Any] =
    baseEndpoint.post
      .in("auth" / "login")
      .errorOut(stringBody)
      .out(jsonBody[User])

  val newUser: Endpoint[User, String, Source[ByteString, Any], Any with AkkaStreams] =
    baseEndpoint.post
      .in("auth" / "login" / "new")
      .in(jsonBody[User])
      .errorOut(stringBody)
      .out(streamBody(AkkaStreams)(Schema(Schema.derived[List[User]].schemaType), CodecFormat.Json()))

  val renewToken: Endpoint[Unit, String, User, Any] =
    baseEndpoint.get
      .in("auth" / "login" / "renew")
      .errorOut(stringBody)
      .out(jsonBody[User])

}

object AuthEndpoint extends AuthEndpoint
