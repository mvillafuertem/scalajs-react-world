package io.github.mvillafuertem.chat.infrastructure

import zio.stream.Stream

trait UserRepository {

  def createUser(dbo: UserDBO): Stream[Throwable, UserDBO]

  def findUserByEmail(email: String): Stream[Throwable, UserDBO]

}
