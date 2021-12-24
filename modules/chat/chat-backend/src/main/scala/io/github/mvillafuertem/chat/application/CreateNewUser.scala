package io.github.mvillafuertem.chat.application

import com.github.t3hnar.bcrypt._
import io.github.mvillafuertem.chat.domain.error.ChatError
import io.github.mvillafuertem.chat.domain.model.User
import io.github.mvillafuertem.chat.infrastructure.MongoUserRepository.ZUserRepository
import io.github.mvillafuertem.chat.infrastructure.{ UserDBO, UserRepository }
import zio.stream.ZStream
import zio.{ stream, Has, Task, ZIO, ZLayer }

final class CreateNewUser private (userRepository: UserRepository) {

  def createUser(user: User): stream.Stream[ChatError, User] =
    for {
      dbo <- ZStream
        .fromEffect(Task.effect(user.password.bcrypt))
        .map(encryptedPassword => UserDBO(user.name, user.email, encryptedPassword))
        .mapError(e => ChatError.ServiceNotAvailable(e.getMessage))
      createdUser <- userRepository
        .createUser(dbo)
        .map(_ => user)
    } yield createdUser

}

object CreateNewUser {

  def apply(userRepository: UserRepository): CreateNewUser = new CreateNewUser(userRepository)

  type ZCreateNewUser = Has[CreateNewUser]

  val live: ZLayer[ZUserRepository, Nothing, ZCreateNewUser] =
    ZLayer.fromService[UserRepository, CreateNewUser](CreateNewUser(_))

  def createUser(user: User): stream.ZStream[ZCreateNewUser, ChatError, User] =
    stream.ZStream.accessStream(_.get.createUser(user))

}
