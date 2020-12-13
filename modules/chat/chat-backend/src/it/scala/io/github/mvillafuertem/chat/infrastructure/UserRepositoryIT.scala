package io.github.mvillafuertem.chat.infrastructure

import com.dimafeng.testcontainers.{DockerComposeContainer, ExposedService}
import com.mongodb.reactivestreams.client.{MongoClient, MongoClients, MongoDatabase}
import io.github.mvillafuertem.chat.configuration.properties.MongoDBConfigurationProperties
import io.github.mvillafuertem.shared.User
import org.testcontainers.containers
import org.testcontainers.containers.wait.strategy.Wait
import zio._
import zio.test.Assertion.equalTo
import zio.test._
import zio.test.environment.TestEnvironment

import java.io.File

object UserRepositoryIT extends DefaultRunnableSpec {

  override def spec: ZSpec[TestEnvironment, Any] =
    (suite(getClass.getSimpleName)(
      testM("create an user")(
        assertM(
          // w h e n
          for {
            _    <- UserRepository.createUser(User("hola", "adios@email.com", "qwerty")).runCollect
            user <- UserRepository.findUserByEmail("adios@email.com").runCollect
          } yield user
          // t h e n
        )(equalTo(Chunk.single(User("hola", "adios@email.com", "qwerty"))))
      ),
      testM("get an user")(
        assertM(
          // w h e n
          for {
            user <- UserRepository.findUserByEmail("adios@email.com").runCollect
          } yield user
          // t h e n
        )(equalTo(Chunk.empty))
      )
    ) @@ TestAspect.around(
      for {
        container <- dockerInfrastructure
        _         <- Task.effect(container.start())
      } yield container
    )(container => Task.effect(container.stop()).run)).provideSomeLayer(userRepositoryITEnv)

  override def aspects: List[TestAspect[Nothing, TestEnvironment, Nothing, Any]] =
    List(TestAspect.executionStrategy(ExecutionStrategy.Sequential))

  private lazy val dockerInfrastructure: Task[containers.DockerComposeContainer[_]] = Task(
    DockerComposeContainer(
      new File(s"modules/chat/chat-backend/src/it/resources/docker-compose.it.yml"),
      exposedServices = Seq(ExposedService("mongo", 27017, 1, Wait.forLogMessage(".*waiting for connections on port .*\\n", 1))),
      identifier = "docker_infrastructure"
    ).container
  )

  private val userRepositoryITEnv = ZLayer.succeed(MongoDBConfigurationProperties()) >+>
    ZLayer.fromService[MongoDBConfigurationProperties, MongoClient] { properties =>
      val connectionString = {
        val user     = properties.user
        val password = properties.password
        val hostname = properties.hostname
        val port     = properties.port
        s"mongodb://$user:$password@$hostname:$port"
      }
      MongoClients.create(connectionString)
    } >>> ZLayer.fromServices[MongoDBConfigurationProperties, MongoClient, MongoDatabase]((properties, client) => client.getDatabase(properties.database)) >>>
    UserRepository.live

}
