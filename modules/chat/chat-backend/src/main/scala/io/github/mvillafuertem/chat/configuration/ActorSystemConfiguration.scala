package io.github.mvillafuertem.chat.configuration
import akka.Done
import akka.actor.BootstrapSetup
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import io.github.mvillafuertem.chat.configuration.properties.ChatConfigurationProperties
import io.github.mvillafuertem.chat.configuration.properties.ChatConfigurationProperties.ZChatConfigurationProperties
import zio.{ Has, Runtime, ZLayer, ZManaged }

import scala.concurrent.ExecutionContext

trait ActorSystemConfiguration {

  type ZActorSystem = Has[ActorSystem[Done]]

  private def live(executionContext: ExecutionContext, chat: ChatConfigurationProperties): ActorSystem[Done] =
    ActorSystem[Done](
      Behaviors.setup[Done] { context =>
        context.setLoggerName(this.getClass)
        context.log.info(s"Starting ${chat.name}... ${"BuildInfo.toJson"}")
        Behaviors.receiveMessage { case Done =>
          context.log.error(s"Server could not start!")
          Behaviors.stopped
        }
      },
      chat.name.toLowerCase(),
      BootstrapSetup().withDefaultExecutionContext(executionContext)
    )

  val live: ZLayer[ZChatConfigurationProperties, Throwable, ZActorSystem] =
    ZManaged
      .runtime[ZChatConfigurationProperties]
      .flatMap { implicit runtime: Runtime[ZChatConfigurationProperties] => make(runtime.environment.get, runtime.platform.executor.asEC) }
      .toLayer

  def make(chat: ChatConfigurationProperties, executionContext: ExecutionContext): ZManaged[Any, Throwable, ActorSystem[Done]] =
    ZManaged.makeEffect(live(executionContext, chat))(_.terminate())

}

object ActorSystemConfiguration extends ActorSystemConfiguration
