package io.github.mvillafuertem.components.journal

import japgolly.scalajs.react.ScalaFnComponent
import japgolly.scalajs.react.vdom.html_<^.{ <, _ }

object JournalScreen {

  val component = ScalaFnComponent[Unit] { _ =>
    <.div(
      <.h1("Journal Screen")
    )
  }

}
