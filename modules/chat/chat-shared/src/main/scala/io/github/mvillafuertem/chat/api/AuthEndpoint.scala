package io.github.mvillafuertem.chat.api

import akka.http.scaladsl.model.{StatusCodes => AkkaStatusCodes}
import akka.stream.scaladsl.Source
import akka.util.ByteString
import io.circe.generic.auto._
import io.github.mvillafuertem.chat.domain.error.ChatError
import io.github.mvillafuertem.chat.domain.error.ChatError.DuplicateEntityError
import io.github.mvillafuertem.chat.domain.model.{Jwt, User}
import sttp.capabilities.akka.AkkaStreams
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._

trait AuthEndpoint {

  private lazy val conflictDescription = AkkaStatusCodes.Conflict.defaultMessage
  private[api] lazy val statusConflict: EndpointOutput.StatusMapping[ChatError.DuplicateEntityError] =
    statusMapping(StatusCode.Conflict, anyJsonBody[ChatError.DuplicateEntityError].example(DuplicateEntityError()).description(conflictDescription))

  private lazy val defaultDescription = "Unknown Error"
  private[api] lazy val statusDefault: EndpointOutput.StatusMapping[ChatError] =
    statusDefaultMapping(anyJsonBody[ChatError].example(ChatError.ServiceNotAvailable()).description(defaultDescription))

  val loginUser: Endpoint[User, ChatError, Source[ByteString, Any], Any with AkkaStreams] =
    baseEndpoint.post
      .in("auth" / "login")
      .in(jsonBody[User])
      .out(streamBody(AkkaStreams)(Schema(Schema.derived[Jwt].schemaType), CodecFormat.Json()))
      .errorOut(oneOf(statusConflict, statusDefault))

  val newUser: Endpoint[User, ChatError, Source[ByteString, Any], Any with AkkaStreams] =
    baseEndpoint.post
      .in("auth" / "login" / "new")
      .in(jsonBody[User])
      .out(streamBody(AkkaStreams)(Schema(Schema.derived[User].schemaType), CodecFormat.Json()))
      .errorOut(oneOf(statusConflict, statusDefault))

  val renewToken: Endpoint[AuthToken, ChatError, Source[ByteString, Any], Any with AkkaStreams] =
    baseEndpoint.get
      .in("auth" / "login" / "renew")
      .in(header[AuthToken](AccessTokenHeaderName))
      .out(streamBody(AkkaStreams)(Schema(Schema.derived[User].schemaType), CodecFormat.Json()))
      .errorOut(oneOf(statusConflict, statusDefault))

}

object AuthEndpoint extends AuthEndpoint
