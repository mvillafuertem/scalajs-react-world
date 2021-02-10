package io.github.mvillafuertem.components

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{ <, ^, _ }
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }

object IncomingMessage {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    <.div(^.className := "incoming_msg")(
      <.div(^.className := "incoming_msg_img")(
        <.img(^.src     := "https://ptetutorials.com/images/user-profile.png", ^.alt := "sunil")
      ),
      <.div(^.className := "received_msg")(
        <.div(^.className := "received_withd_msg")(
          <.p("Test which is a new approach to have all solutions"),
          <.span(^.className := "time_date")("11:01 AM | June")
        )
      )
    )
  }

}
