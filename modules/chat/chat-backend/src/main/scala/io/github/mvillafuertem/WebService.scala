package io.github.mvillafuertem

import akka.http.scaladsl.model.ContentType
import akka.http.scaladsl.model.HttpCharsets.`UTF-8`
import akka.http.scaladsl.model.MediaTypes.`text/html`
import akka.http.scaladsl.server.{Directives, Route}

class WebService() extends Directives {

  def assets: Route =
    getFromResourceDirectory("assets") ~ pathSingleSlash {
      get {
        getFromResource("assets/index.html", ContentType(`text/html`, `UTF-8`))
      }
    }

  val route = assets
}
