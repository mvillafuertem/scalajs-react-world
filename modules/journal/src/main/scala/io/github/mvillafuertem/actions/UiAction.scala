package io.github.mvillafuertem.actions

import io.github.mvillafuertem.facade.ReduxFacade.Extractor
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
      __obj.asInstanceOf[SetError].set("msgError", msgError)
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
      __obj.asInstanceOf[RemoveError].set("msgError", msgError)
    }

  }

  @js.native
  trait StartLoading extends UiAction {
    val loading: Boolean = js.native
  }

  object StartLoading extends Extractor[StartLoading] {
    protected val _type = "[Ui] StartLoading"

    @scala.inline
    override def apply(): StartLoading = {
      val __obj = js.Dynamic.literal()
      __obj.updateDynamic("type")(_type.asInstanceOf[js.Any])
      __obj.asInstanceOf[StartLoading].set("loading", true)
    }
  }

  @js.native
  trait FinishLoading extends UiAction {
    val loading: Boolean = js.native
  }

  object FinishLoading extends Extractor[FinishLoading] {
    protected val _type = "[Ui] StartLoading"

    @scala.inline
    override def apply(): FinishLoading = {
      val __obj = js.Dynamic.literal()
      __obj.updateDynamic("type")(_type.asInstanceOf[js.Any])
      __obj.asInstanceOf[FinishLoading].set("loading", false)
    }

  }

}
