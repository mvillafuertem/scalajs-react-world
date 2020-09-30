package io.github.mvillafuertem.components.journal

import japgolly.scalajs.react.ScalaFnComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scala.scalajs.js


object JournalEntry {

  val component = ScalaFnComponent[Unit] { _ =>

    <.div(^.className := "journal__entry pointer")(
      <.div(
        ^.className := "journal__entry-picture",
        ^.style := js.Dictionary(
          "backgroundSize" -> "cover",
          "backgroundImage" -> "url(https://p.bigstockphoto.com/GeFvQkBbSLaMdpKXF1Zv_bigstock-Aerial-View-Of-Blue-Lakes-And--227291596.jpg)")
      ),
      <.div(^.className:="journal__entry-body")(
        <.p(^.className:="journal__entry-title")("Un nuevo día"),
        <.div(^.className:="journal__entry-content")("Lorem Ipsum has been the industry's standard dummy text ever since the 1500s"),
        <.div(^.className:="journal__entry-date-box")(
          <.span("Monday"),
          <.h4(28)
        )
      )
    )

  }

}
