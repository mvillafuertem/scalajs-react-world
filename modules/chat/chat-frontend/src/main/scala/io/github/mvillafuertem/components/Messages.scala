package io.github.mvillafuertem.components

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{ <, ^, _ }
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }

object Messages {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    <.div(^.className := "mesgs")(
      <.div(^.className := "msg_history")(
        IncomingMessage.component(),
        OutgoingMessage.component(),
      ),
      SendMessage.component()
    )
  }

}
