package io.github.mvillafuertem.components.journal

import japgolly.scalajs.react.ScalaFnComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

object Sidebar {

  val component = ScalaFnComponent[Unit] { _ =>
    <.aside(^.className := "journal__sidebar")(
      <.div(^.className := "journal__sidebar-navbar")(
        <.h3(^.className := "mt-5")(
          <.i(^.className := "far fa-moon"),
          <.span(" Pepe")
        ),
        <.button(^.className := "btn")("Logout")
      ),
      <.div(^.className := "journal__new-entry")(
        <.i(^.className := "far fa-calendar-plus fa-5x"),
        <.p("New entry")
      ),
      JournalEntries.component()
    )
  }

}
