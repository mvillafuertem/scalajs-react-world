package io.github.mvillafuertem.chat.configuration

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ContentType
import akka.http.scaladsl.model.HttpCharsets.`UTF-8`
import akka.http.scaladsl.model.MediaTypes.`text/html`
import akka.http.scaladsl.server.Directives.{ getFromResource, getFromResourceDirectory, pathEndOrSingleSlash }
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.RouteConcatenation._
import akka.stream.Materializer
import akka.{ actor, Done }
import io.github.mvillafuertem.chat.configuration.ActorSystemConfiguration.ZActorSystem
import io.github.mvillafuertem.chat.configuration.properties.AkkaHttpServerConfigurationProperties
import io.github.mvillafuertem.chat.configuration.properties.AkkaHttpServerConfigurationProperties.ZAkkaHttpServerConfigurationProperties
import zio.{ Has, ZLayer, ZManaged }

import scala.concurrent.duration._
import scala.concurrent.{ ExecutionContext, Future }

trait AkkaHttpServerConfiguration {

  val live: ZLayer[ZActorSystem with Has[ExecutionContext] with ZAkkaHttpServerConfigurationProperties, Throwable, Has[
    Future[Http.ServerBinding]
  ]] =
    ZLayer.fromServicesManaged[
      ActorSystem[Done],
      ExecutionContext,
      //ApiConfiguration,
      AkkaHttpServerConfigurationProperties,
      Any,
      Throwable,
      Future[Http.ServerBinding]
    ]((actorSystem, executionContext, properties) => make(actorSystem, executionContext, properties))

  def assets: Route =
    pathEndOrSingleSlash {
      getFromResource("assets/index.html", ContentType(`text/html`, `UTF-8`))
    } ~ getFromResourceDirectory("assets")

  val route = assets

  def make(
    actorSystem:      ActorSystem[_],
    executionContext: ExecutionContext,
    //api:              ApiConfiguration,
    properties: AkkaHttpServerConfigurationProperties
  ): ZManaged[Any, Throwable, Future[Http.ServerBinding]] =
    ZManaged.makeEffect {
      implicit lazy val untypedSystem: actor.ActorSystem = actorSystem.toClassic
      implicit lazy val materializer:  Materializer      = Materializer(actorSystem)
      Http().newServerAt(properties.interface, properties.port).bind(route)
    }(_.map(_.terminate(10.second))(executionContext))
      .tapError(exception =>
        ZManaged.succeed(actorSystem.log.error(s"Server could not start with parameters [host:port]=[${properties.interface},${properties.port}]", exception))
      )
      .tap(future =>
        ZManaged.succeed(
          future.map(serverBinding => actorSystem.log.info(s"Server online at http:/${serverBinding.localAddress}"))(executionContext)
        )
      )
}

object AkkaHttpServerConfiguration extends AkkaHttpServerConfiguration
