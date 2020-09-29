package io.github.mvillafuertem.states

import scala.scalajs.js

class UiState(val loading: Boolean, val msgError: String) extends js.Object

object UiState {

  val initial: UiState = UiState(_loading = false, "")

  def apply(_loading: Boolean, _msgError: String): UiState =
    new UiState(_loading, _msgError)
}
