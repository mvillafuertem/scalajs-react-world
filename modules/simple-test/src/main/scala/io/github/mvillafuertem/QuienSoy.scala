package io.github.mvillafuertem

import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.html.{div, h1, h2}

@react object QuienSoy {

  type Props = Unit

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { _ =>
    div(
      h1("Quien soy")(
        h2("Pruebas")
      )
    )
  }


}