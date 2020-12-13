package io.github.mvillafuertem.chat.infrastructure

import com.mongodb.reactivestreams.client.{ MongoCollection, MongoDatabase, Success }
import io.github.mvillafuertem.shared.User
import org.bson.codecs.configuration.CodecRegistries.{ fromProviders, fromRegistries }
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import zio.interop.reactivestreams._
import zio.stream._
import zio.{ stream, Has, ULayer, ZLayer }

final class UserRepository(mongoDatabase: MongoDatabase) {

  val collectionName = "users"

  val collection: MongoCollection[User] = mongoDatabase
    .getCollection(collectionName, classOf[User])
    .withCodecRegistry(fromRegistries(fromProviders(classOf[User]), DEFAULT_CODEC_REGISTRY))

  def createUser(user: User): ZStream[Any, Throwable, Success] = collection.insertOne(user).toStream()

  def findUserByEmail(email: String): ZStream[Any, Throwable, User] = collection.find().toStream().filter(_.email == email)

}

object UserRepository {

  def apply(mongoDatabase: MongoDatabase): UserRepository =
    new UserRepository(mongoDatabase)

  type ZUserRepository = Has[UserRepository]

  def createUser(user: User): stream.ZStream[ZUserRepository, Throwable, Success] =
    stream.ZStream.accessStream(_.get.createUser(user))

  def findUserByEmail(email: String): stream.ZStream[ZUserRepository, Throwable, User] =
    stream.ZStream.accessStream(_.get.findUserByEmail(email))

  val live: ZLayer[Has[MongoDatabase], Nothing, ZUserRepository] =
    ZLayer.fromService[MongoDatabase, UserRepository](UserRepository(_))

  def make(mongoDatabase: MongoDatabase): ULayer[ZUserRepository] =
    ZLayer.succeed(mongoDatabase) >>> live
}
