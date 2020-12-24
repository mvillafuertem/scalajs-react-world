package io.github.mvillafuertem.chat.application

import io.github.mvillafuertem.chat.infrastructure.MongoUserRepository.ZUserRepository
import io.github.mvillafuertem.chat.infrastructure.{ UserDBO, UserRepository }
import io.github.mvillafuertem.shared.User
import zio.{ stream, Has, ZLayer }

final class CreateNewUser private (userRepository: UserRepository) {

  def createUser(user: User): stream.Stream[Throwable, User] = userRepository
    .createUser(UserDBO(user.name, user.email, user.password))
    .map(_ => user)

}

object CreateNewUser {

  def apply(userRepository: UserRepository): CreateNewUser = new CreateNewUser(userRepository)

  type ZCreateNewUser = Has[CreateNewUser]

  val live: ZLayer[ZUserRepository, Nothing, ZCreateNewUser] =
    ZLayer.fromService[UserRepository, CreateNewUser](CreateNewUser(_))

  def createUser(user: User): stream.ZStream[ZCreateNewUser, Throwable, User] =
    stream.ZStream.accessStream(_.get.createUser(user))

}
