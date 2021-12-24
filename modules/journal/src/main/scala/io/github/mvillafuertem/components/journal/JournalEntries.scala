package io.github.mvillafuertem.components.journal

import japgolly.scalajs.react.ScalaFnComponent
import japgolly.scalajs.react.vdom.html_<^.{ <, _ }

import scala.scalajs.js

object JournalEntries {

  val component = ScalaFnComponent[Unit] { _ =>
    val entries = js.Array[Int](1, 2, 3, 4, 5, 6, 7, 8, 9, 0)

    <.div(^.className := "journal__entries")(
      entries.map(value => JournalEntry.component.withKey(value)()).toVdomArray
    )
  }
}
