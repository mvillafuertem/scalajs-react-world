package io.github.mvillafuertem.reducers

import io.github.mvillafuertem.actions.AuthAction
import io.github.mvillafuertem.model.Person
import io.github.mvillafuertem.states.AuthState
import typings.redux.mod.Reducer

import scala.scalajs.js

object AuthReducer {

  val default: Reducer[AuthState, AuthAction] = (stateOpt, action) => {
    val state = stateOpt.getOrElse(AuthState.initial)
    println(js.JSON.stringify(action))
    action match {
      case AuthAction.Login(_action) => AuthState(_action.person)
      case AuthAction.Logout(_)      => AuthState(Person.default)
      case _                         => state
    }
  }

}
