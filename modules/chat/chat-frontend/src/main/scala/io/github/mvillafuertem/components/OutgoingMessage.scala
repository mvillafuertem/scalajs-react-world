package io.github.mvillafuertem.components

import japgolly.scalajs.react.{CtorType, ScalaFnComponent}
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{<, ^}
import japgolly.scalajs.react.vdom.html_<^._

object OutgoingMessage {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    <.div(^.className := "outgoing_msg")(
      <.div(^.className := "sent_msg")(
        <.p("Test which is a new approach to have al solutions"),
        <.span(^.className := "time_date")("11:01 AM | June")
      )
    )
  }

}
