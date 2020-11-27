package io.github.mvillafuertem.components.heroes

import io.github.mvillafuertem.model.Hero
import io.github.mvillafuertem.model.Hero
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.html.{p, _}
import typings.reactRouterDom.components.{Link, Switch, HashRouter => Router}

import scala.scalajs.js

@react object HeroCard {

  case class Props(hero: Hero)

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { case Props(hero) =>
    div(className := "card ms-3", style := js.Dynamic.literal(maxWidth = "540"))(
      div(className := "row no-gutters")(
        div(className := "col-md-4")(
          img(src := s"./assets/heroes/${hero.id}.jpg", className := "card-img", alt := hero.superhero)
        ),
        div(className := "col-md-8")(
          div(className := "card-body")(
            h5(className := "card-title")(hero.superhero),
            p(className := "card-text")(hero.alter_ego),
            if (!hero.alter_ego.equalsIgnoreCase(hero.characters))
              p(className := "card-text")(hero.characters)
            else p(),
            p(className := "text-muted")(hero.first_appearance),
            Link[String](s"./hero/${hero.id}")("more info...")
          )
        )
      )
    )
  }

}
