package io.github.mvillafuertem.chat.configuration

import io.github.mvillafuertem.chat.configuration.ActorSystemConfiguration.ZActorSystem
import io.github.mvillafuertem.chat.configuration.ApiConfiguration.ZApiConfiguration
import io.github.mvillafuertem.chat.configuration.properties.AkkaHttpServerConfigurationProperties.ZAkkaHttpServerConfigurationProperties
import io.github.mvillafuertem.chat.configuration.properties.{AkkaHttpServerConfigurationProperties, ChatConfigurationProperties}
import io.github.mvillafuertem.chat.configuration.properties.ChatConfigurationProperties.ZChatConfigurationProperties
import zio.{ExitCode, Has, UIO, URLayer, ZIO, ZLayer}
import zio.clock.Clock
import zio.console.Console
import zio.internal.Platform
import zio.logging.{LogFormat, LogLevel, Logging}
import zio.logging.log

import scala.concurrent.ExecutionContext

trait ChatServiceConfiguration {

  private lazy val loggingLayer: URLayer[Console with Clock, Logging] =
    Logging.console(
      logLevel = LogLevel.Info,
      format = LogFormat.ColoredLogFormat()
    ) >>> Logging.withRootLoggerName("ChatServiceApplication")

  private lazy val actorSystemLayer
  : ZLayer[Any, Throwable, Has[ExecutionContext]
    with ZChatConfigurationProperties
    with ZAkkaHttpServerConfigurationProperties
    //with ZApiConfiguration
    with ZActorSystem] =
    ZLayer.succeed[ExecutionContext](Platform.default.executor.asEC) >+>
      ZLayer.succeed[ChatConfigurationProperties](ChatConfigurationProperties()) >+>
      ZLayer.succeed[AkkaHttpServerConfigurationProperties](AkkaHttpServerConfigurationProperties()) >+>
      //ApiChatConfiguration.live >+>
      ActorSystemConfiguration.live

  val chatServiceApplication: ZIO[Console with Clock, Nothing, ExitCode] =
    AkkaHttpServerConfiguration.live.build.useForever
      .provideLayer(actorSystemLayer)
      .foldM(e => log.throwable("", e).as(ExitCode.failure), _ => UIO.effectTotal(ExitCode.success))
      .provideLayer(loggingLayer)



}
