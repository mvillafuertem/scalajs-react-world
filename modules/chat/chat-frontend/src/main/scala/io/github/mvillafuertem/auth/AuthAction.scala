package io.github.mvillafuertem.auth

import io.github.mvillafuertem.chat.domain.model.User

sealed trait AuthAction

object AuthAction {
  case class Login(user: User) extends AuthAction
  case class Logout(user: User) extends AuthAction
}
