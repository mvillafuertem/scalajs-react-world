package io.github.mvillafuertem.components

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{ <, ^, _ }
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }

object SideBarItem {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    <.div(^.className := "chat_list active_chat")(
      <.div(^.className := "chat_people")(
        <.div(^.className := "chat_img")(
          <.img(^.src     := "https://ptetutorials.com/images/user-profile.png", ^.alt := "sunil")
        ),
        <.div(^.className := "chat_ib")(
          <.h5("Some random name"),
          <.span(^.className := "text-success")("Online"),
          <.span(^.className := "text-danger")("Offline")
        )
      )
    )
  }

}
