package io.github.mvillafuertem.components

import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{ <, ^ }
import japgolly.scalajs.react.vdom.html_<^._

object SideBar {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    <.div(^.className := "inbox_chat")(
//        <.div(^.className := "chat_list")(
//          <.div(^.className := "chat_people")(
//            <.div(^.className := "chat_img")(
//              <.img(^.src     := "https://ptetutorials.com/images/user-profile.png", ^.alt := "sunil")
//            ),
//            <.div(^.className := "chat_ib")(
//              <.h5("Sunil Rajput", <.span(^.className := "chat_date")("Dec 2")),
//              <.p("Test, which is a new approach to have all solution astrology under one roof.")
//            )
//          )
//        ),
      SideBarItem.component(),
      SideBarItem.component(),
      SideBarItem.component(),
      SideBarItem.component(),
      <.div(^.className := "extra_space")
    )

  }

}
