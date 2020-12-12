package io.github.mvillafuertem

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.mongodb.scaladsl.MongoSource
import akka.stream.scaladsl.{Sink, Source}
import com.mongodb.reactivestreams.client.{MongoClient, MongoClients}
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._

import scala.concurrent.{ExecutionContextExecutor, Future}

object InfrastructureConfiguration extends App {

  case class Number(_id: Int)

  java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(java.util.logging.Level.SEVERE)

  val codecRegistry = fromRegistries(fromProviders(classOf[Number]), DEFAULT_CODEC_REGISTRY)

  val mongoDBConfigurationProperties: MongoDBConfigurationProperties = MongoDBConfigurationProperties()

  private val connectionString = {
    val user     = mongoDBConfigurationProperties.user
    val password = mongoDBConfigurationProperties.password
    val hostname = mongoDBConfigurationProperties.hostname
    val port     = mongoDBConfigurationProperties.port
    s"mongodb://$user:$password@$hostname:$port"
  }

  val client: MongoClient = MongoClients.create(connectionString)
  private val db     = client.getDatabase("MongoSourceSpec")
  private val numbersColl = db
    .getCollection("numbers", classOf[Number])
    .withCodecRegistry(codecRegistry)

  implicit val system = ActorSystem()
  implicit val dispatcher: ExecutionContextExecutor = system.dispatcher
  implicit val mat = ActorMaterializer()

  val source: Source[Number, NotUsed] =
    MongoSource(numbersColl.find(classOf[Number]))

  val rows: Future[Seq[Number]] = source.runWith(Sink.seq)

  rows.map(println)

}
