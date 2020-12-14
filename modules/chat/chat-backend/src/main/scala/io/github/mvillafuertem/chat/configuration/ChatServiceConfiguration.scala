package io.github.mvillafuertem.chat.configuration

import io.github.mvillafuertem.chat.configuration.ActorSystemConfiguration.ZActorSystem
import io.github.mvillafuertem.chat.configuration.properties.AkkaHttpServerConfigurationProperties.ZAkkaHttpServerConfigurationProperties
import io.github.mvillafuertem.chat.configuration.properties.ChatConfigurationProperties.ZChatConfigurationProperties
import io.github.mvillafuertem.chat.configuration.properties.{ AkkaHttpServerConfigurationProperties, ChatConfigurationProperties }
import zio._
import zio.clock.Clock
import zio.console.Console
import zio.logging.{ log, LogFormat, LogLevel, Logging }

trait ChatServiceConfiguration {

  private lazy val loggingLayer: URLayer[Console with Clock, Logging] =
    Logging.console(
      logLevel = LogLevel.Info,
      format = LogFormat.ColoredLogFormat()
    ) >>> Logging.withRootLoggerName("ChatServiceApplication")

  private lazy val actorSystemLayer: ZLayer[
    Any,
    Throwable,
    ZChatConfigurationProperties with ZAkkaHttpServerConfigurationProperties
    //with ZApiConfiguration
    with ZActorSystem
  ] =
    ZLayer.succeed[ChatConfigurationProperties](ChatConfigurationProperties()) >+>
      ZLayer.succeed[AkkaHttpServerConfigurationProperties](AkkaHttpServerConfigurationProperties()) >+>
      //ApiChatConfiguration.live >+>
      ZEnv.live >+> ActorSystemConfiguration.live

  val chatServiceApplication: URIO[ZEnv, ExitCode] =
    AkkaHttpServerConfiguration.live.build.useForever
      .provideSomeLayer[ZEnv](actorSystemLayer)
      .foldM(e => log.throwable("", e).as(ExitCode.failure).provideLayer(loggingLayer), _ => UIO.effectTotal(ExitCode.success))

}
