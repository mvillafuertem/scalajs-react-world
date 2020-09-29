package io.github.mvillafuertem.reducers

import io.github.mvillafuertem.ReduxFacade.Extractor
import io.github.mvillafuertem.hooks.Person
import typings.redux.mod.Action

import scala.scalajs.js


@js.native
sealed trait UiAction extends Action[String]

object UiAction {

  @js.native
  trait SetError extends UiAction {
    val msgError: String = js.native
  }

  object SetError extends Extractor[SetError] {
    protected val _type = "[Ui] SetError"

    @scala.inline
    def apply(msgError: String): SetError = {
      val __obj = js.Dynamic.literal()
      __obj.updateDynamic("type")(_type.asInstanceOf[js.Any])
      __obj.asInstanceOf[SetError]
    }

  }

  @js.native
  trait RemoveError extends UiAction {
    val msgError: String = js.native
  }

  object RemoveError extends Extractor[RemoveError] {
    protected val _type = "[Ui] RemoveError"

    @scala.inline
    def apply(msgError: String): RemoveError = {
      val __obj = js.Dynamic.literal()
      __obj.updateDynamic("type")(_type.asInstanceOf[js.Any])
      __obj.asInstanceOf[RemoveError]
    }

  }

}
