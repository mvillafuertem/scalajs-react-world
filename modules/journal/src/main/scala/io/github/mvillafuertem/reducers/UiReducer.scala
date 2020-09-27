package io.github.mvillafuertem.reducers

import io.github.mvillafuertem.hooks.Person
import io.github.mvillafuertem.reducers.AuthAction.{Login, Logout}
import typings.redux.mod.Reducer

import scala.scalajs.js

object UiReducer {

  val Reducer: Reducer[UiState, UiAction] = (stateOpt, action) => {
    val state = stateOpt.getOrElse(UiState.initial)
    println(js.JSON.stringify(action))
    action match {
      case UiAction.SetError(_action) => UiState(state.loading, _action.msgError)
      case UiAction.RemoveError(_action) => UiState(state.loading, "")
      case _ => state
    }
  }

}
