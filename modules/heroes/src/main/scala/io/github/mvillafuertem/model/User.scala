package io.github.mvillafuertem.model

import scala.scalajs.js

class User(val name: String, val logged: Boolean) extends js.Object

object User {
  def apply(name: String = "", logged: Boolean = false): User = new User(name, logged)
}
