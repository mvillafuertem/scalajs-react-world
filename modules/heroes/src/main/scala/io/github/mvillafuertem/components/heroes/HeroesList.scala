package io.github.mvillafuertem.components.heroes

import io.github.mvillafuertem.model.Hero.getHeroByPublisher
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Hooks.useMemo
import slinky.web.html.{ className, div }

@react object HeroesList {

  case class Props(publisher: String)

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { case Props(publisher) =>
    val heroes = useMemo(() => getHeroByPublisher(publisher), Seq(publisher))
    div(className := "card-columns animate__animated animate__fadeIn")(
      heroes.map(hero => HeroCard(hero).withKey(hero.id))
    )
  }
}
