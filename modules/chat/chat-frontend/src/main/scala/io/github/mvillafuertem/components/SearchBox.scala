package io.github.mvillafuertem.components

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{ <, ^, _ }
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }

object SearchBox {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    <.div(^.className := "headind_srch")(
      <.div(^.className := "recent_heading mt-2")(
        <.h4("Reciente")
      ),
      <.div(^.className := "srch_bar")(
        <.div(^.className := "stylish-input-group")(
          <.button(^.className := "btn text-danger")(
            "Salir"
          )
        )
      )
    )
  }

}
