package io.github.mvillafuertem.states

import io.github.mvillafuertem.model.Person

import scala.scalajs.js

class AuthState(val person: Person) extends js.Object

object AuthState {

  val initial: AuthState = AuthState(Person.default)

  def apply(_person: Person): AuthState =
    new AuthState(_person)
}
