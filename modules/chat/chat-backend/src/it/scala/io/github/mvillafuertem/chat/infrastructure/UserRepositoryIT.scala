package io.github.mvillafuertem.chat.infrastructure

import com.dimafeng.testcontainers.{ DockerComposeContainer, ExposedService }
import com.mongodb.reactivestreams.client.{ MongoClient, MongoClients, MongoDatabase }
import io.github.mvillafuertem.chat.configuration.properties.MongoDBConfigurationProperties
import io.github.mvillafuertem.shared.User
import org.testcontainers.containers
import org.testcontainers.containers.wait.strategy.Wait
import zio._
import zio.test.Assertion.equalTo
import zio.test._
import zio.test.environment.TestEnvironment

import java.io.File

trait UserRepositoryConfigurationIT extends DefaultRunnableSpec {

  val mongoDBConfigurationProperties: MongoDBConfigurationProperties = MongoDBConfigurationProperties()

  private val connectionString = {
    val user     = mongoDBConfigurationProperties.user
    val password = mongoDBConfigurationProperties.password
    val hostname = mongoDBConfigurationProperties.hostname
    val port     = mongoDBConfigurationProperties.port
    s"mongodb://$user:$password@$hostname:$port"
  }

  val client:        MongoClient   = MongoClients.create(connectionString)
  val mongoDatabase: MongoDatabase = client.getDatabase(mongoDBConfigurationProperties.database)

  //val repository = new UserRepository(mongoDatabase)

  def dockerInfrastructure: containers.DockerComposeContainer[_] =
    DockerComposeContainer(
      new File(s"modules/chat/chat-backend/src/it/resources/docker-compose.it.yml"),
      exposedServices = Seq(ExposedService("mongo", mongoDBConfigurationProperties.port, 1, Wait.forLogMessage(".*waiting for connections on port .*\\n", 1))),
      identifier = "docker_infrastructure"
    ).container

}

object UserRepositoryIT extends UserRepositoryConfigurationIT {

  override def spec: ZSpec[TestEnvironment, Any] =
    suite(getClass.getSimpleName)(testM("create an user") {
      assertM {
        // w h e n
        for {
          container <- Task.effect(dockerInfrastructure)
          _         <- Task.effect(container.start())
          _         <- UserRepository.createUser(User("hola", "adios@email.com", "qwerty")).runCollect
          user      <- UserRepository.findUserByEmail("adios@email.com").runCollect
          _         <- Task.effect(container.stop())
        } yield user
        // t h e n
      }(equalTo(Chunk.single(User("hola", "adios@email.com", "qwerty"))))
    }).provideSomeLayer(UserRepository.make(mongoDatabase))

}
