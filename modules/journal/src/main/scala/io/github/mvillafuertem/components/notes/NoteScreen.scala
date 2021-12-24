package io.github.mvillafuertem.components.notes

import japgolly.scalajs.react.ScalaFnComponent
import japgolly.scalajs.react.vdom.html_<^.{ <, _ }

object NoteScreen {

  val component = ScalaFnComponent[Unit] { _ =>
    <.div(^.className := "notes__main-content")(
      NotesAppBar.component(),
      <.div(^.className          := "notes__content")(
        <.input(^.`type`         := "text", ^.placeholder              := "Some awesome title", ^.className := "notes__title-input", ^.autoComplete := "off"),
        <.textarea(^.placeholder := "What happened today", ^.className := "notes__textarea"),
        <.div(^.className := "notes__image")(
          <.img(
            ^.src := "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9a/Gull_portrait_ca_usa.jpg/1600px-Gull_portrait_ca_usa.jpg",
            ^.alt := "imagen"
          )
        )
      )
    )
  }

}
