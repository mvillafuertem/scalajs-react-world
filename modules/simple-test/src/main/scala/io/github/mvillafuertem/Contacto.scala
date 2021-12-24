package io.github.mvillafuertem

import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.html.div
import slinky.web.html.h1

@react object Contacto {

  type Props = Unit

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { _ =>
    div(
      h1("Estamos en contacto")
    )
  }

}
