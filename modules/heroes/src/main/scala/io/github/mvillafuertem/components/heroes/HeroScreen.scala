package io.github.mvillafuertem.components.heroes

import io.github.mvillafuertem.model.Hero.getHeroById
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Hooks.useMemo
import slinky.web.html.{ button, _ }
import typings.history.mod.{ History, LocationState }
import typings.reactRouterDom.components.Redirect
import typings.reactRouterDom.mod.useParams

import scala.scalajs.js

@react object HeroScreen {

  case class Props(history: History[LocationState])

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { case Props(history) =>
    val params: js.Dictionary[String] = useParams()

    val handleReturn = () => if (history.length <= 2) history.push("/") else history.goBack()

    val heroId = useMemo(() => params.get("heroId"), Seq(params))

    heroId
      .flatMap(getHeroById)
      .fold(Redirect("/").build)(hero =>
        div(className := "row mt-5")(
          div(className := "col-4")(
            img(
              src := s"../assets/heroes/${hero.id}.jpg",
              alt := hero.superhero,
              className := "img-thumbnail animate__animated animate__fadeInLeft"
            )
          ),
          div(className := "col-8 animate__animated animate__fadeIn")(
            h3(hero.superhero),
            ul(className := "list-group list-group-flush")(
              li(className := "list-group-item")(s"Alter ego: ${hero.alter_ego}"),
              li(className := "list-group-item")(s"Publisher: ${hero.publisher}"),
              li(className := "list-group-item")(s"First appearance: ${hero.first_appearance}")
            ),
            h5("Characters"),
            p(hero.characters),
            button(className := "btn btn-outline-info", onClick := handleReturn)("Return")
          )
        )
      )

  }
}
