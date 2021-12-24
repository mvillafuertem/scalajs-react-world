package io.github.mvillafuertem.components.journal

import japgolly.scalajs.react.ScalaFnComponent
import japgolly.scalajs.react.vdom.html_<^.{ <, _ }
import japgolly.scalajs.react.vdom.html_<^.{ <, _ }

object NothingSelected {

  val component = ScalaFnComponent[Unit] { _ =>
    <.div(^.className := "nothing__main-content")(
      <.p(
        "Select something",
        <.hr(),
        "pr create an entry!"
      ),
      <.i(^.className := "far fa-star fa-4x mt-5")
    )
  }

}
