package io.github.mvillafuertem.chat.configuration

import io.github.mvillafuertem.chat.application.{AuthenticateUser, CreateNewUser, ManageToken}
import io.github.mvillafuertem.chat.application.CreateNewUser.ZCreateNewUser
import io.github.mvillafuertem.chat.configuration
import io.github.mvillafuertem.chat.configuration.ApiConfiguration.ZApiConfiguration
import io.github.mvillafuertem.chat.configuration.properties.{AkkaHttpServerConfigurationProperties, ChatConfigurationProperties}
import io.github.mvillafuertem.chat.infrastructure.{MongoUserRepository, UserRepository, ZMongoDatabase}
import zio._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console.Console
import zio.logging.{LogFormat, LogLevel, Logging, log}

trait ChatServiceConfiguration extends InfrastructureConfiguration {

  private lazy val loggingLayer: URLayer[Console with Clock, Logging] =
    Logging.console(
      logLevel = LogLevel.Info,
      format = LogFormat.ColoredLogFormat()
    ) >>> Logging.withRootLoggerName("ChatServiceApplication")

  private lazy val actorSystemLayer
    : ZLayer[Blocking, Throwable, Has[ChatConfigurationProperties] with Has[AkkaHttpServerConfigurationProperties] with ZMongoDatabase with Has[
      UserRepository
    ] with ZCreateNewUser with ZApiConfiguration with configuration.ActorSystemConfiguration.ZActorSystem] =
    ZLayer.succeed[ChatConfigurationProperties](ChatConfigurationProperties()) >+>
      ZLayer.succeed[AkkaHttpServerConfigurationProperties](AkkaHttpServerConfigurationProperties()) >+>
      mongoDBLayer >+>
      ZLayer.succeed("secretKey") >+>
      ManageToken.live >+>
      MongoUserRepository.live >+>
      AuthenticateUser.live >+>
      CreateNewUser.live >+>
      ApiConfiguration.live >+>
      ActorSystemConfiguration.live

  val chatServiceApplication: URIO[ZEnv, ExitCode] =
    AkkaHttpServerConfiguration.live.build.useForever
      .provideSomeLayer[ZEnv](actorSystemLayer)
      .foldM(e => log.throwable("", e).as(ExitCode.failure).provideLayer(loggingLayer), _ => UIO.effectTotal(ExitCode.success))

}
