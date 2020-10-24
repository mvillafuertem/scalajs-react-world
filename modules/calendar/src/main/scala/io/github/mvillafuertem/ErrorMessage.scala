package io.github.mvillafuertem

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }
import typings.reactstrap.components.Alert

object ErrorMessage {

  case class ErrorMessageProps(debug: String = "", message: String = "")

  val component: Component[ErrorMessageProps, CtorType.Props] = ScalaFnComponent[ErrorMessageProps] { props =>
    Alert().color("danger")(
      <.p(^.className := "mb-3")(props.message),
      <.pre(^.className := "alert-pre border bg-light p-2")(<.code(props.debug)).when(props.debug.nonEmpty)
    )
  }

}
