package io.github.mvillafuertem.components

import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{ <, ^ }
import japgolly.scalajs.react.vdom.html_<^._

object ChatSelect {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    <.div(^.className := "middle-screen")(
      <.div(^.className := "alert-info")(
        <.hr(),
        <.h3("Choose any contact"),
        <.span("to start")
      )
    )
  }

}
