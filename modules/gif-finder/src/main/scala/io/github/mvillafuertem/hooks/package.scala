package io.github.mvillafuertem

import io.circe
import io.circe.Json
import sttp.client.circe.asJson
import sttp.client.{ FetchBackend, Identity, NothingT, RequestT, ResponseError, SttpBackend, basicRequest, _ }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

package object hooks {

  implicit val sttpBackend: SttpBackend[Future, Nothing, NothingT] = FetchBackend()

  def url(q: String) = s"https://api.giphy.com/v1/gifs/search?q=$q&api_key=SRzBE7nFmZXTklicDjG9DbMV7dxZC9Pf&limit=5"

  def requestGET(q: String): RequestT[Identity, Either[ResponseError[circe.Error], Json], Nothing] = basicRequest.get(uri"${url(q)}").response(asJson[Json])

  def getGifts(category: String): Future[Either[ResponseError[circe.Error], Json]] = requestGET(category).send().map(_.body)

  //def getGifts(category: String) = Fetch.fetch(url(category))

}
