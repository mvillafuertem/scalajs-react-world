package io.github.mvillafuertem.chat.infrastructure

import akka.Done
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.alpakka.mongodb.scaladsl.MongoSource
import akka.stream.scaladsl.{ Sink, Source }
import com.dimafeng.testcontainers.{ DockerComposeContainer, ExposedService }
import com.mongodb.reactivestreams.client.{ MongoClient, MongoClients, MongoDatabase }
import io.github.mvillafuertem.MongoDBConfigurationProperties
import io.github.mvillafuertem.chat.infrastructure.UserRepositorySpec.UserRepositoryConfigurationIT
import io.github.mvillafuertem.shared.User
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AsyncFlatSpecLike
import org.scalatest.matchers.should.Matchers
import org.testcontainers.containers
import org.testcontainers.containers.wait.strategy.Wait

import java.io.File
import scala.concurrent.ExecutionContextExecutor

final class UserRepositorySpec extends UserRepositoryConfigurationIT {

  behavior of s"${this.getClass.getSimpleName}"

  it should "save with insertOne" in {

    for {
      _ <- repository
        .createUser(User("hola", "adios@email.com", "qwerty"))
        .map(_ shouldBe Done)

      actual <- MongoSource(repository.collection.find())
        .filter(_.name == "hola")
        .runWith(Sink.head)
    } yield actual.email shouldBe "adios@email.com"

  }

}

object UserRepositorySpec {

  trait UserRepositoryConfigurationIT extends AsyncFlatSpecLike with Matchers with BeforeAndAfterAll {
    implicit val actorSystem:  ActorSystem              = ActorSystem("MongoDBIT")
    implicit val dispatcher:   ExecutionContextExecutor = actorSystem.dispatcher
    implicit val materializer: Materializer             = Materializer(actorSystem)

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

    val repository = new UserRepository(mongoDatabase)

    var container: containers.DockerComposeContainer[_] = _

    def dockerInfrastructure: containers.DockerComposeContainer[_] =
      DockerComposeContainer(
        new File(s"modules/chat/chat-backend/src/it/resources/docker-compose.it.yml"),
        exposedServices =
          Seq(ExposedService("mongo", mongoDBConfigurationProperties.port, 1, Wait.forLogMessage(".*waiting for connections on port .*\\n", 1))),
        identifier = "docker_infrastructure"
      ).container

    override protected def beforeAll(): Unit = {
      container = dockerInfrastructure
      container.start()
      Source.fromPublisher(mongoDatabase.drop()).runWith(Sink.head).map(_ shouldBe Done)
    }

    override protected def afterAll(): Unit = {
      container.stop()
      actorSystem.terminate().map(_.actor.path.address.system shouldBe "asdfasd").map(println)
    }

  }

}
