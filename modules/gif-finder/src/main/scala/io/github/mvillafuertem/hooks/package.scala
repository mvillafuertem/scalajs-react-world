package io.github.mvillafuertem

import io.circe
import io.circe.Json
import sttp.capabilities
import sttp.client3.circe.asJson
import sttp.client3.{FetchBackend, Identity, RequestT, SttpBackend, basicRequest, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

package object hooks {

  private val sttpBackend: SttpBackend[Future, capabilities.WebSockets] = FetchBackend()

  def url(q: String) = s"https://api.giphy.com/v1/gifs/search?q=$q&api_key=SRzBE7nFmZXTklicDjG9DbMV7dxZC9Pf&limit=5"

  def requestGET(q: String): RequestT[Identity, Either[ResponseException[String, circe.Error], Json], Any] = basicRequest.get(uri"${url(q)}").response(asJson[Json])

  def getGifts(category: String): Future[Either[ResponseException[String, circe.Error], Json]] = requestGET(category).send(sttpBackend).map(_.body)

  // def getGifts(category: String) = Fetch.fetch(url(category))

}
