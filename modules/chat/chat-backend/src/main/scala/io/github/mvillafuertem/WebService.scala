package io.github.mvillafuertem

import akka.http.scaladsl.server.Directives
import io.github.mvillafuertem.shared.SharedMessages
import io.github.mvillafuertem.twirl.Implicits._

class WebService() extends Directives {

  val route = {
    pathSingleSlash {
      get {
        complete {
          io.github.mvillafuertem.html.index.render(SharedMessages.itWorks)
        }
      }
    } ~
      pathPrefix("assets" / Remaining) { file =>
        // optionally compresses the response with Gzip or Deflate
        // if the client accepts compressed responses
        encodeResponse {
          getFromResource("public/" + file)
        }
      }
  }
}
