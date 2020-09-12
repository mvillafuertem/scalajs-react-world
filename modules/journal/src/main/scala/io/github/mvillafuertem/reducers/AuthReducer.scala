package io.github.mvillafuertem.reducers

object AuthReducer {

  sealed trait AuthAction
  case object Login  extends AuthAction
  case object Logout extends AuthAction

  def apply(state: String, action: AuthAction): String =
    action match {
      case Login  => state
      case Logout => state
      case _      => state
    }

}
