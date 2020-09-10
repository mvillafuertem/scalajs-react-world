package io.github.mvillafuertem.components.marvel

import io.github.mvillafuertem.model.Hero.getHeroByPublisher
import io.github.mvillafuertem.components.heroes.HeroesList
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Fragment
import slinky.web.html.{ h1, hr, p }

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

@react object MarvelScreen {

  type Props = Unit

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { _ =>
    Fragment(
      h1("MarvelScreen"),
      hr(),
      HeroesList("Marvel Comics")
    )
  }
}
