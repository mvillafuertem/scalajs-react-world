package io.github.mvillafuertem.chat.infrastructure

import com.dimafeng.testcontainers.{ DockerComposeContainer, ExposedService }
import io.github.mvillafuertem.chat.configuration.InfrastructureConfiguration
import io.github.mvillafuertem.chat.configuration.properties.MongoDBConfigurationProperties
import io.github.mvillafuertem.chat.configuration.properties.MongoDBConfigurationProperties.ZMongoDBConfigurationProperties
import io.github.mvillafuertem.chat.infrastructure.RunnableIntegrationSpec._
import org.testcontainers.containers
import org.testcontainers.containers.wait.strategy.Wait
import zio._
import zio.duration.durationInt
import zio.test.environment.TestEnvironment
import zio.test.{ TestAspect, _ }

import java.io.File

trait RunnableIntegrationSpec extends RunnableSpec[ZIntegrationSpecEnv, Any] with InfrastructureConfiguration {

  override def aspects: List[TestAspect[Nothing, ZIntegrationSpecEnv, Nothing, Any]] =
    List(
      TestAspect.timeout(60.seconds),
      TestAspect.executionStrategy(ExecutionStrategy.Sequential),
      TestAspect.around(
        (for {
          container <- ZIO.access[ZDockerInfrastructure](_.get)
          _         <- Task.effect(container.start())
        } yield container).orDie
      )(container => Task.effect(container.stop()).orDie)
    )

  override def runner: TestRunner[ZIntegrationSpecEnv, Any] =
    TestRunner(TestExecutor.default(integrationTestLayer))

  val integrationTestLayer: ULayer[ZIntegrationSpecEnv] =
    (ZLayer.succeed(MongoDBConfigurationProperties()) >+>
      dockerInfrastructureLayer >+>
      zio.test.environment.testEnvironment >+>
      mongoDBLayer).orDie

  private lazy val dockerInfrastructureLayer: ZLayer[ZMongoDBConfigurationProperties, Throwable, ZDockerInfrastructure] =
    ZLayer.fromService[MongoDBConfigurationProperties, containers.DockerComposeContainer[_]](properties =>
      DockerComposeContainer(
        new File(s"modules/chat/chat-backend/src/it/resources/docker-compose.it.yml"),
        exposedServices = Seq(ExposedService("mongo", properties.port, 1, Wait.forLogMessage(".*waiting for connections on port .*\\n", 1))),
        identifier = "docker_infrastructure"
      ).container
    )

}
object RunnableIntegrationSpec {

  type ZDockerInfrastructure = Has[containers.DockerComposeContainer[_]]
  type ZIntegrationSpecEnv   = TestEnvironment with ZDockerInfrastructure with ZMongoDatabase

}
