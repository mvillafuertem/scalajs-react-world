package io.github.mvillafuertem.chat.api

import akka.stream.scaladsl.Source
import akka.http.scaladsl.model.{StatusCodes => AkkaStatusCodes}
import akka.util.ByteString
import io.circe.generic.auto._
import io.github.mvillafuertem.chat.model.error.ChatError
import io.github.mvillafuertem.chat.model.error.ChatError.DuplicateEntityError
import io.github.mvillafuertem.shared.User
import sttp.capabilities.akka.AkkaStreams
import sttp.capabilities.zio.ZioStreams
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.model.UsernamePassword
import zio.stream
import sttp.model.StatusCode

trait AuthEndpoint {

  private lazy val conflictDescription = AkkaStatusCodes.Conflict.defaultMessage
  private[api] lazy val statusConflict: EndpointOutput.StatusMapping[ChatError.DuplicateEntityError] =
    statusMapping(StatusCode.Conflict, anyJsonBody[ChatError.DuplicateEntityError].example(DuplicateEntityError()).description(conflictDescription))

  private lazy val defaultDescription = "Unknown Error"
  private[api] lazy val statusDefault: EndpointOutput.StatusMapping[ChatError] =
    statusDefaultMapping(anyJsonBody[ChatError].example(ChatError.ServiceNotAvailable()).description(defaultDescription))

  val loginUser: Endpoint[Unit, String, User, Any] =
    baseEndpoint.post
      .in("auth" / "login")
      .errorOut(stringBody)
      .out(jsonBody[User])

  val newUser: Endpoint[User, ChatError, Source[ByteString, Any], Any with AkkaStreams] =
    baseEndpoint.post
      .in("auth" / "login" / "new")
      .in(jsonBody[User])
      .out(streamBody(AkkaStreams)(Schema(Schema.derived[User].schemaType), CodecFormat.Json()))
      .errorOut(oneOf(statusConflict, statusDefault))

  val renewToken: Endpoint[Unit, String, User, Any] =
    baseEndpoint.get
      .in("auth" / "login" / "renew")
      .errorOut(stringBody)
      .out(jsonBody[User])

}

object AuthEndpoint extends AuthEndpoint
