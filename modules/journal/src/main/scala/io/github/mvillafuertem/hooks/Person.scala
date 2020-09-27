package io.github.mvillafuertem.hooks

import scala.scalajs.js

class Person(val uid: String, val name: String, val email: String, val password: String) extends js.Object

object Person {

  val default: Person = apply()

  def apply(uid: String = "", name: String = "", email: String = "", password: String = ""): Person = new Person(uid, name, email, password)

}

