package io.github.mvillafuertem.components

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{ <, ^, _ }
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }

object SendMessage {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    <.form(
      <.div(^.className := "type_msg row")(
        <.div(^.className := "input_msg_write col-sm-9")(<.input(^.`type` := "text", ^.className := "write_msg", ^.placeholder := "Mensaje...")),
        <.div(^.className := "col-sm-3 text-center")(<.button(^.className := "msg_send_btn mt-3", ^.`type` := "button")("enviar"))
      )
    )
  }

}
