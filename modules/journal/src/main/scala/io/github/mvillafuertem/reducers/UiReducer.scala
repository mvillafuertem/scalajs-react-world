package io.github.mvillafuertem.reducers

import typings.redux.mod.Reducer

object UiReducer {

  val Reducer: Reducer[UiState, UiAction] = (stateOpt, action) => {
    val state = stateOpt.getOrElse(UiState.initial)
    action match {
      case UiAction.SetError(_action) => UiState(state.loading, _action.msgError)
      case UiAction.RemoveError(_action) => UiState(state.loading, "")
      case _ => state
    }
  }

}
