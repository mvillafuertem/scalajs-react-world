package io.github.mvillafuertem.chat.infrastructure

import com.mongodb.reactivestreams.client.{MongoCollection, MongoDatabase}
import io.github.mvillafuertem.chat.domain.error.ChatError
import org.mongodb.scala.MongoWriteException
import zio.interop.reactivestreams._
import zio.stream._
import zio.{Has, ZLayer, stream}

final class MongoUserRepository private (mongoDatabase: MongoDatabase) extends UserRepository {

  private val COLLECTION_NAME = "users"

  private val collection: MongoCollection[UserDBO] = mongoDatabase
    .getCollection(COLLECTION_NAME, classOf[UserDBO])

  def createUser(dbo: UserDBO): Stream[ChatError, UserDBO] =
    collection
      .insertOne(dbo)
      .toStream()
      .map(result => dbo.copy(_id = Some(result.getInsertedId.asObjectId().getValue)))
      .mapError {
        case e: MongoWriteException if e.getCode == 11000 =>
          ChatError.DuplicateEntityError()
      }

  def findUserByEmail(email: String): Stream[Throwable, UserDBO] =
    collection
      .find()
      .toStream()
      .filter(_.email == email)

}

object MongoUserRepository {

  def apply(mongoDatabase: MongoDatabase): UserRepository =
    new MongoUserRepository(mongoDatabase)

  type ZUserRepository = Has[UserRepository]

  def createUser(dbo: UserDBO): ZStream[ZUserRepository, ChatError, UserDBO] =
    stream.ZStream.accessStream(_.get.createUser(dbo))

  def findUserByEmail(email: String): stream.ZStream[ZUserRepository, Throwable, UserDBO] =
    stream.ZStream.accessStream(_.get.findUserByEmail(email))

  val live: ZLayer[ZMongoDatabase, Nothing, ZUserRepository] =
    ZLayer.fromService[MongoDatabase, UserRepository](MongoUserRepository(_))

}
