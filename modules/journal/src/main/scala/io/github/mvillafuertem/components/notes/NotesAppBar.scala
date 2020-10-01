package io.github.mvillafuertem.components.notes

import japgolly.scalajs.react.ScalaFnComponent
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}

object NotesAppBar {

  val component = ScalaFnComponent[Unit] { _ =>
    <.div(^.className:="notes__appbar")(
      <.span("28 de agosto 2020"),
      <.div(
        <.button(^.className:="btn")("Picture"),
        <.button(^.className:="btn")("Save"),
      )
    )
  }

}
