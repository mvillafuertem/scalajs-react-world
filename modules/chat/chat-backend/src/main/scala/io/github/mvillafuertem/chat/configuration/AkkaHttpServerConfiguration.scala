package io.github.mvillafuertem.chat.configuration

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.stream.Materializer
import akka.{ actor, Done }
import io.github.mvillafuertem.chat.configuration.ActorSystemConfiguration.ZActorSystem
import io.github.mvillafuertem.chat.configuration.ApiConfiguration.ZApiConfiguration
import io.github.mvillafuertem.chat.configuration.properties.AkkaHttpServerConfigurationProperties
import io.github.mvillafuertem.chat.configuration.properties.AkkaHttpServerConfigurationProperties.ZAkkaHttpServerConfigurationProperties
import zio.{ Has, Runtime, ZEnv, ZLayer, ZManaged }

import scala.concurrent.Future
import scala.concurrent.duration._

trait AkkaHttpServerConfiguration {

  val live: ZLayer[ZEnv with ZActorSystem with ZApiConfiguration with ZAkkaHttpServerConfigurationProperties, Throwable, Has[
    Future[Http.ServerBinding]
  ]] =
    ZLayer.fromServicesManaged[
      ActorSystem[Done],
      ApiConfiguration,
      AkkaHttpServerConfigurationProperties,
      ZEnv,
      Throwable,
      Future[Http.ServerBinding]
    ]((actorSystem, api, properties) => make(actorSystem, api, properties))

  def make(
    actorSystem: ActorSystem[_],
    api:         ApiConfiguration,
    properties:  AkkaHttpServerConfigurationProperties
  ): ZManaged[ZEnv, Throwable, Future[Http.ServerBinding]] =
    ZManaged.runtime[ZEnv].flatMap { implicit runtime: Runtime[ZEnv] =>
      ZManaged.makeEffect {
        implicit lazy val untypedSystem: actor.ActorSystem = actorSystem.toClassic
        implicit lazy val materializer:  Materializer      = Materializer(actorSystem)
        Http().newServerAt(properties.interface, properties.port).bind(api.routes)
      }(_.map(_.terminate(10.second))(runtime.platform.executor.asEC))
        .tapError(exception =>
          ZManaged.succeed(actorSystem.log.error(s"Server could not start with parameters [host:port]=[${properties.interface},${properties.port}]", exception))
        )
        .tap(future =>
          ZManaged.succeed(
            future.map(serverBinding => actorSystem.log.info(s"Server online at http:/${serverBinding.localAddress}"))(runtime.platform.executor.asEC)
          )
        )
    }
}

object AkkaHttpServerConfiguration extends AkkaHttpServerConfiguration
