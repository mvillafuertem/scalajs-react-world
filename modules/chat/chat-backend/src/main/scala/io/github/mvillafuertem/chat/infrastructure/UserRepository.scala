package io.github.mvillafuertem.chat.infrastructure

import akka.Done
import akka.stream.Materializer
import akka.stream.alpakka.mongodb.scaladsl.MongoSink
import akka.stream.scaladsl.Source
import com.mongodb.reactivestreams.client.{ MongoCollection, MongoDatabase }
import io.github.mvillafuertem.shared.User
import org.bson.codecs.configuration.CodecRegistries.{ fromProviders, fromRegistries }
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._

import scala.concurrent.Future

final class UserRepository(mongoDatabase: MongoDatabase)(implicit materializer: Materializer) {

  val codecRegistry = fromRegistries(fromProviders(classOf[User]), DEFAULT_CODEC_REGISTRY)

  val collection: MongoCollection[User] = mongoDatabase.getCollection("numbersSink", classOf[User]).withCodecRegistry(codecRegistry)

  def createUser(user: User): Future[Done] = Source.single(user).runWith(MongoSink.insertOne(collection))

}
