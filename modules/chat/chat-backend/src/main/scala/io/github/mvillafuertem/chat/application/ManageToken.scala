package io.github.mvillafuertem.chat.application

import io.github.mvillafuertem.chat.domain.error.ChatError
import io.github.mvillafuertem.chat.domain.model.{ Jwt, User }
import pdi.jwt.{ JwtAlgorithm, JwtCirce, JwtClaim }
import zio.stream.ZStream
import zio.{ stream, Has, Task, ZLayer }

import java.time.Instant
import java.time.temporal.ChronoUnit

final class ManageToken private (secretKey: String) {

  private val algorithm = JwtAlgorithm.HS256

  def isTokenValid(token: String): stream.Stream[ChatError, Jwt] =
    (
      for {
        expired <- isExpiredToken(token)
        valid   <- ZStream.fromEffect(Task.effect(JwtCirce.isValid(token, secretKey, Seq(algorithm))))
        jwt     <- generateToken(User("", "", "")) if valid && expired
      } yield jwt
    ).mapError(_ => ChatError.ServiceNotAvailable())

  def generateToken(user: User): stream.Stream[ChatError, Jwt] =
    ZStream
      .fromEffect(Task.effect {
        val now        = Instant.now
        val expiration = now.plus(2, ChronoUnit.HOURS).getEpochSecond
        val issuedAt   = now.getEpochSecond
        val claim: JwtClaim = JwtClaim(
          content = s"""{"username": "${user.email}"}""",
          expiration = Some(expiration),
          issuedAt = Some(issuedAt)
        )
        val token = JwtCirce.encode(claim, secretKey, algorithm)
        Jwt(token, expiration, issuedAt)
      })
      .mapError(_ => ChatError.ServiceNotAvailable())

  def isExpiredToken(token: String): stream.Stream[ChatError, Boolean] =
    ZStream
      .fromEffect(
        Task.fromTry(
          JwtCirce
            .decode(token, secretKey, Seq(algorithm))
            .map(_.expiration.exists(_ < Instant.now.getEpochSecond))
        )
      )
      .mapError(_ => ChatError.ServiceNotAvailable())

}

object ManageToken {

  def apply(secretKey: String): ManageToken = new ManageToken(secretKey)

  type ZManageToken = Has[ManageToken]
  type ZSecretKey   = Has[String]

  def isTokenValid(token: String): stream.ZStream[ZManageToken, ChatError, Jwt] =
    stream.ZStream.accessStream(_.get.isTokenValid(token))

  def generateToken(user: User): stream.ZStream[ZManageToken, ChatError, Jwt] =
    stream.ZStream.accessStream(_.get.generateToken(user))

  def isExpiredToken(token: String): stream.ZStream[ZManageToken, ChatError, Boolean] =
    stream.ZStream.accessStream(_.get.isExpiredToken(token))

  val live: ZLayer[ZSecretKey, Nothing, ZManageToken] =
    ZLayer.fromService[String, ManageToken](ManageToken(_))

}
