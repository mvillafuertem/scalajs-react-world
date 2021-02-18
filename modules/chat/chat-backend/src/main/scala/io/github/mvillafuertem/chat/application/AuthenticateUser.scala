package io.github.mvillafuertem.chat.application

import com.github.t3hnar.bcrypt._
import io.github.mvillafuertem.chat.application.ManageToken.ZManageToken
import io.github.mvillafuertem.chat.domain.error.ChatError
import io.github.mvillafuertem.chat.domain.model.{ Jwt, User }
import io.github.mvillafuertem.chat.infrastructure.MongoUserRepository.ZUserRepository
import io.github.mvillafuertem.chat.infrastructure.UserRepository
import zio.stream.ZStream
import zio.{ stream, Has, ZLayer }

final class AuthenticateUser private (manageToken: ManageToken, userRepository: UserRepository) {

  def authenticateUser(user: User): ZStream[Any, ChatError, Jwt] =
    (for {
      encryptedPassword <- userRepository.findUserByEmail(user.email).map(_.password)
      jwt               <- manageToken.generateToken(user) if user.password.isBcrypted(encryptedPassword)
    } yield jwt).mapError(_ => ChatError.ServiceNotAvailable())

  def renewToken(token: String): ZStream[Any, ChatError, Jwt] =
    (for {
      validToken <- manageToken.isTokenValid(token)
    } yield validToken).mapError(_ => ChatError.UnauthorizedError())

}

object AuthenticateUser {

  def apply(manageToken: ManageToken, userRepository: UserRepository): AuthenticateUser =
    new AuthenticateUser(manageToken, userRepository)

  type ZAuthenticateUser = Has[AuthenticateUser]

  val live: ZLayer[ZManageToken with ZUserRepository, Nothing, ZAuthenticateUser] =
    ZLayer.fromServices[ManageToken, UserRepository, AuthenticateUser](AuthenticateUser(_, _))

  def authenticateUser(user: User): stream.ZStream[ZAuthenticateUser, ChatError, Jwt] =
    stream.ZStream.accessStream(_.get.authenticateUser(user))

  def renewToken(token: String): stream.ZStream[ZAuthenticateUser, ChatError, Jwt] =
    stream.ZStream.accessStream(_.get.renewToken(token))

}
