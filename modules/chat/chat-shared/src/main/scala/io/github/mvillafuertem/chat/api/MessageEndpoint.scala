package io.github.mvillafuertem.chat.api

import io.circe.generic.auto._
import io.github.mvillafuertem.chat.domain.model.Message
import sttp.capabilities.akka.AkkaStreams
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._

trait MessageEndpoint {

  val getAllMessages: Endpoint[Unit, String, Seq[Message], Any] =
    baseEndpoint.post
      .in("messages")
      .errorOut(stringBody)
      .out(jsonBody[Seq[Message]])

  val webSocketStreams =
    baseEndpoint
    .in(isWebSocket)
    .errorOut(stringBody)
    .out(webSocketBody[String, CodecFormat.TextPlain, String, CodecFormat.TextPlain](AkkaStreams))

}

object MessageEndpoint extends MessageEndpoint
