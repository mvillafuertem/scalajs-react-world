package io.github.mvillafuertem.chat.api

import akka.http.scaladsl.model.{ StatusCodes => AkkaStatusCodes }
import akka.stream.scaladsl.Source
import akka.util.ByteString
import io.circe.Encoder
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import io.github.mvillafuertem.chat.domain.error.ChatError
import io.github.mvillafuertem.chat.domain.error.ChatError.{ DuplicateEntityError, NonExistentEntityError, ServiceNotAvailable, UnauthorizedError }
import io.github.mvillafuertem.chat.domain.model.{ Jwt, User }
import sttp.capabilities.akka.AkkaStreams
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._

trait AuthEndpoint {

  private implicit val encodeChatError: Encoder[ChatError] = Encoder.instance {
    case x @ DuplicateEntityError(_)   => x.asJson
    case x @ NonExistentEntityError(_) => x.asJson
    case x @ ServiceNotAvailable(_)    => x.asJson
    case x @ UnauthorizedError(_)      => x.asJson
  }

  private lazy val conflictDescription     = AkkaStatusCodes.Conflict.defaultMessage
  private lazy val notFoundDescription     = AkkaStatusCodes.NotFound.defaultMessage
  private lazy val unauthorizedDescription = AkkaStatusCodes.Unauthorized.defaultMessage
  private lazy val defaultDescription      = "Unknown Error"

  private[api] lazy val statusConflict: EndpointOutput.StatusMapping[ChatError.DuplicateEntityError] =
    statusMapping(StatusCode.Conflict, jsonBody[ChatError.DuplicateEntityError].example(DuplicateEntityError()).description(conflictDescription))

  private[api] lazy val notFound: EndpointOutput.StatusMapping[ChatError.NonExistentEntityError] =
    statusMapping(StatusCode.NotFound, jsonBody[ChatError.NonExistentEntityError].example(NonExistentEntityError()).description(notFoundDescription))

  private[api] lazy val unauthorized: EndpointOutput.StatusMapping[ChatError.UnauthorizedError] =
    statusMapping(StatusCode.Unauthorized, jsonBody[ChatError.UnauthorizedError].example(UnauthorizedError()).description(unauthorizedDescription))

  private[api] lazy val statusDefault: EndpointOutput.StatusMapping[ChatError] =
    statusDefaultMapping(jsonBody[ChatError].example(ChatError.ServiceNotAvailable()).description(defaultDescription))

  val loginUser: Endpoint[User, ChatError, Source[ByteString, Any], Any with AkkaStreams] =
    baseEndpoint.post
      .in("auth" / "login")
      .in(jsonBody[User])
      .out(streamBody(AkkaStreams)(Schema(Schema.derived[Jwt].schemaType), CodecFormat.Json()))
      .errorOut(oneOf(notFound, statusConflict, statusDefault))

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
      .errorOut(oneOf(unauthorized, statusDefault))

}

object AuthEndpoint extends AuthEndpoint
