package io.github.mvillafuertem.reducers

import io.github.mvillafuertem.actions.UiAction
import io.github.mvillafuertem.states.UiState
import typings.redux.mod.Reducer

import scala.scalajs.js

object UiReducer {

  val default: Reducer[UiState, UiAction] = (stateOpt, action) => {
    val state = stateOpt.getOrElse(UiState.initial)
    action match {
      case UiAction.SetError(_action)    => UiState(state.loading, _action.msgError)
      case UiAction.RemoveError(_action) => UiState(state.loading, "")
      case _                             => state
    }
  }

}
