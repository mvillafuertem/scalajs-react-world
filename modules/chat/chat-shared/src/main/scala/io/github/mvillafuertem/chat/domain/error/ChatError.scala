package io.github.mvillafuertem.chat.domain.error

import java.time.Instant
import java.util.UUID

sealed trait ChatError extends Product { self =>

  val id: UUID = UUID.randomUUID()

  val creationDate: Instant = Instant.now()

  val code: String = self match {
    case ChatError.InternalServerError(_)    => "InternalServerError"
    case ChatError.ServiceNotAvailable(_)    => "ServiceNotAvailable"
    case ChatError.DuplicateEntityError(_)   => "DuplicateEntityError"
    case ChatError.NonExistentEntityError(_) => "NonExistentEntityError"
  }

  val message: String

}

object ChatError {

  final case class InternalServerError(message: String = "") extends ChatError

  final case class ServiceNotAvailable(message: String = "") extends ChatError

  final case class DuplicateEntityError(message: String = "") extends ChatError

  final case class NonExistentEntityError(message: String = "") extends ChatError

}
