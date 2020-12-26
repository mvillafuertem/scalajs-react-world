package io.github.mvillafuertem.chat.infrastructure

import io.github.mvillafuertem.chat.domain.error.ChatError
import zio.stream.Stream

trait UserRepository {

  def createUser(dbo: UserDBO): Stream[ChatError, UserDBO]

  def findUserByEmail(email: String): Stream[Throwable, UserDBO]

}
