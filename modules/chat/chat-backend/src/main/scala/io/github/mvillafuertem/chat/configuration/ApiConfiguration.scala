package io.github.mvillafuertem.chat.configuration

import akka.http.scaladsl.server.Route
import zio.Has

trait ApiConfiguration {

  type ZApiConfiguration = Has[ApiConfiguration]

  val routes: Route = ???

}

object ApiConfiguration extends ApiConfiguration
