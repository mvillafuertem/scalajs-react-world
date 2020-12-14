package io.github.mvillafuertem.chat.infrastructure

import com.dimafeng.testcontainers.{ DockerComposeContainer, ExposedService }
import com.mongodb.reactivestreams.client.{ MongoClient, MongoClients, MongoDatabase }
import io.github.mvillafuertem.chat.configuration.properties.MongoDBConfigurationProperties
import io.github.mvillafuertem.chat.infrastructure.RunnableIntegrationSpec._
import org.testcontainers.containers
import org.testcontainers.containers.wait.strategy.Wait
import zio._
import zio.duration.durationInt
import zio.test._
import zio.test.environment.TestEnvironment

import java.io.File

trait RunnableIntegrationSpec extends RunnableSpec[ZIntegrationSpecEnv, Any] {

  override def aspects: List[TestAspect[Nothing, ZIntegrationSpecEnv, Nothing, Any]] =
    List(TestAspect.timeout(60.seconds), TestAspect.executionStrategy(ExecutionStrategy.Sequential))

  override def runner: TestRunner[ZIntegrationSpecEnv, Any] =
    TestRunner(TestExecutor.default(integrationTestLayer))

  val integrationTestLayer: ULayer[ZIntegrationSpecEnv] =
    (ZLayer.succeed(MongoDBConfigurationProperties()) >+>
      dockerInfrastructureLayer >+>
      mongoClientLayer >+>
      zio.test.environment.testEnvironment >+>
      mongoDatabaseLayer).orDie

  private lazy val dockerInfrastructureLayer: ZLayer[ZMongoDBConfigurationProperties, Throwable, ZDockerInfrastructure] =
    ZLayer.fromService[MongoDBConfigurationProperties, containers.DockerComposeContainer[_]](properties =>
      DockerComposeContainer(
        new File(s"modules/chat/chat-backend/src/it/resources/docker-compose.it.yml"),
        exposedServices = Seq(ExposedService("mongo", properties.port, 1, Wait.forLogMessage(".*waiting for connections on port .*\\n", 1))),
        identifier = "docker_infrastructure"
      ).container
    )

  private lazy val mongoDatabaseLayer: ZLayer[ZMongoDBConfigurationProperties with ZMongoClient, Nothing, ZMongoDatabase] =
    ZLayer.fromServices[MongoDBConfigurationProperties, MongoClient, MongoDatabase]((properties, client) => client.getDatabase(properties.database))

  private lazy val mongoClientLayer: ZLayer[ZMongoDBConfigurationProperties, Nothing, ZMongoClient] =
    ZLayer.fromService[MongoDBConfigurationProperties, MongoClient] { properties =>
      val connectionString = {
        val user     = properties.user
        val password = properties.password
        val hostname = properties.hostname
        val port     = properties.port
        s"mongodb://$user:$password@$hostname:$port"
      }
      MongoClients.create(connectionString)
    }

}
object RunnableIntegrationSpec {

  type ZDockerInfrastructure           = Has[containers.DockerComposeContainer[_]]
  type ZMongoDatabase                  = Has[MongoDatabase]
  type ZMongoClient                    = Has[MongoClient]
  type ZMongoDBConfigurationProperties = Has[MongoDBConfigurationProperties]
  type ZMongo                          = ZMongoDatabase with ZMongoClient with ZMongoDBConfigurationProperties
  type ZIntegrationSpecEnv             = TestEnvironment with ZDockerInfrastructure with ZMongo

}
