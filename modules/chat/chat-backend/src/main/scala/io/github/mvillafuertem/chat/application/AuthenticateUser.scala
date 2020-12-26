package io.github.mvillafuertem.chat.application

import com.github.t3hnar.bcrypt._
import io.github.mvillafuertem.chat.domain.error.ChatError
import io.github.mvillafuertem.chat.domain.model.{ Jwt, User }
import io.github.mvillafuertem.chat.infrastructure.MongoUserRepository.ZUserRepository
import io.github.mvillafuertem.chat.infrastructure.UserRepository
import pdi.jwt.{ JwtAlgorithm, JwtCirce, JwtClaim }
import zio.stream.ZStream
import zio.{ stream, Has, Task, ZLayer }

import java.time.Instant
import java.time.temporal.ChronoUnit

final class AuthenticateUser(userRepository: UserRepository) {

  private val key       = "secretKey"
  private val algorithm = JwtAlgorithm.HS256

  def authenticateUser(user: User): ZStream[Any, ChatError, Jwt] =
    (for {
      encryptedPassword <- userRepository.findUserByEmail(user.email).map(_.password)
      jwt <- ZStream.fromEffect(Task.effect {
        val now        = Instant.now
        val expiration = now.plus(2, ChronoUnit.HOURS).getEpochSecond
        val issuedAt   = now.getEpochSecond
        val claim: JwtClaim = JwtClaim(
          content = s"""{"username": "${user.email}"}""",
          expiration = Some(expiration),
          issuedAt = Some(issuedAt)
        )
        val token = JwtCirce.encode(claim, key, algorithm)
        Jwt(token, expiration, issuedAt)
      }) if user.password.isBcrypted(encryptedPassword)
    } yield jwt).mapError(_ => ChatError.ServiceNotAvailable())

}

object AuthenticateUser {

  def apply(userRepository: UserRepository): AuthenticateUser = new AuthenticateUser(userRepository)

  type ZAuthenticateUser = Has[AuthenticateUser]

  val live: ZLayer[ZUserRepository, Nothing, ZAuthenticateUser] =
    ZLayer.fromService[UserRepository, AuthenticateUser](AuthenticateUser(_))

  def authenticateUser(user: User): stream.ZStream[ZAuthenticateUser, ChatError, Jwt] =
    stream.ZStream.accessStream(_.get.authenticateUser(user))

}
