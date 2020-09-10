package io.github.mvillafuertem.components.dc

import io.github.mvillafuertem.components.heroes.HeroesList
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Fragment
import slinky.web.html.{h1, hr}

@react object DcScreen {

  type Props = Unit

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { _ =>
    Fragment(
      h1("DcScreen"),
      hr(),
      HeroesList("Dc Comics")
    )
  }
}
