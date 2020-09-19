package io.github.mvillafuertem.hooks

import scala.scalajs.js

class Person(val name: String, val email: String, val password: String) extends js.Object

object Person {

  val default: Person = apply()

  def apply(name: String = "", email: String = "", password: String = ""): Person = new Person(name, email, password)

}

