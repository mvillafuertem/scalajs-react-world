package io.github.mvillafuertem.auth

import io.github.mvillafuertem.model.User

object AuthReducer {

  def apply(user: User, action: AuthAction): User =
    action match {
      case AuthAction.Login(user) => User(user.name, logged = true)
      case AuthAction.Logout(_)            => User()
      case _                               => user
    }

}
