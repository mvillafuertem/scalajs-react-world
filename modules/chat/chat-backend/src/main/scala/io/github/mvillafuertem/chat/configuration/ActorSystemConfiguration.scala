package io.github.mvillafuertem.chat.configuration
import akka.Done
import akka.actor.BootstrapSetup
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import io.github.mvillafuertem.chat.configuration.properties.ChatConfigurationProperties
import io.github.mvillafuertem.chat.configuration.properties.ChatConfigurationProperties.ZChatConfigurationProperties
import zio.{ Has, ZEnv, ZLayer, ZManaged }

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

  val live: ZLayer[ZEnv with ZChatConfigurationProperties, Throwable, ZActorSystem] =
    ZLayer.fromServiceManaged[ChatConfigurationProperties, ZEnv, Throwable, ActorSystem[Done]](make)

  def make(chat: ChatConfigurationProperties): ZManaged[ZEnv, Throwable, ActorSystem[Done]] =
    ZManaged.runtime[ZEnv].flatMap { implicit runtime: zio.Runtime[ZEnv] =>
      ZManaged.makeEffect(live(runtime.platform.executor.asEC, chat))(_.terminate())
    }

}

object ActorSystemConfiguration extends ActorSystemConfiguration
