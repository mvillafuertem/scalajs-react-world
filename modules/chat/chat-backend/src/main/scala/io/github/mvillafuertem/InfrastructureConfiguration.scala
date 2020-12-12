package io.github.mvillafuertem

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.mongodb.scaladsl.{MongoSink, MongoSource}
import akka.stream.scaladsl.{Sink, Source}
import com.mongodb.reactivestreams.client.{MongoClient, MongoClients, MongoDatabase}
import org.bson.Document
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
    s"mongodb://$hostname:$port"
  }

  val client: MongoClient = MongoClients.create(connectionString)
  private val db: MongoDatabase = client.getDatabase("chat")
  private val numbersColl = db
    .getCollection("users", classOf[Number])
    .withCodecRegistry(codecRegistry)

  implicit val system = ActorSystem()
  implicit val dispatcher: ExecutionContextExecutor = system.dispatcher
  implicit val mat = ActorMaterializer()

  val source: Source[Number, NotUsed] =
    MongoSource(numbersColl.find(classOf[Number]))

  val rows: Future[Seq[Number]] = source.runWith(Sink.seq)

  rows.map(println)
  val testRange = 0 until 10
  val numbersDocumentColl                              = db.getCollection("numbersSink")


  val s: Source[Document, NotUsed] = Source(testRange).map(i => Document.parse(s"""{"value":$i}"""))
  val completion = s.runWith(MongoSink.insertOne(numbersDocumentColl))

  completion.map(a => assert(a == Done))
  val found: Future[Seq[Document]] = Source.fromPublisher(numbersDocumentColl.find()).runWith(Sink.seq)

  private val future: Future[Seq[Integer]] = for {
    a <- Source.fromPublisher(numbersDocumentColl.find()).runWith(Sink.seq)
    b <- Future(a.map(_.getInteger("value")))
  } yield b

  future.map(println)

}
