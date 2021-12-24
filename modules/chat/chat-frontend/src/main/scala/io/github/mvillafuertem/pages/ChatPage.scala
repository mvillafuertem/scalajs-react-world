package io.github.mvillafuertem.pages

import io.github.mvillafuertem.components.{ ChatSelect, InboxPeople, Messages }
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{ <, _ }
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }

object ChatPage {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    <.div(^.className := "messaging")(
      <.div(^.className := "inbox_msg")(
        InboxPeople.component(),
        ChatSelect.component()
        // Messages.component()
      )
    )

  }

}
