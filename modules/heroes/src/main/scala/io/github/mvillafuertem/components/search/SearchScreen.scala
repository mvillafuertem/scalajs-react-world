package io.github.mvillafuertem.components.search

import io.github.mvillafuertem.components.heroes.HeroCard
import io.github.mvillafuertem.model.Hero.heroes
import io.github.mvillafuertem.hooks.useForm
import org.scalajs.dom.Event
import slinky.core.annotations.react
import slinky.core.facade.Hooks
import slinky.core.{ FunctionalComponent, SyntheticEvent, TagElement }
import slinky.web.html._
import typings.history.mod.{ History, LocationState }
import typings.queryString.mod.parse
import typings.reactRouter.mod.useLocation

import scala.scalajs.js

@react object SearchScreen {

  case class Props(history: History[LocationState])

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { case Props(history) =>
    val location = useLocation()

    val q                               = parse(location.search).get("q").fold("")(_.asInstanceOf[String])
    val (searchText, handleInputChange) = useForm(q)

    val filteredHeroes = Hooks.useMemo(
      () =>
        heroes.filter(
          _.superhero.toLowerCase
            .contains(q.toLowerCase)
        ),
      Seq(q)
    )

    val handleSearch: SyntheticEvent[TagElement#RefType, Event] => Unit =
      (e: SyntheticEvent[TagElement#RefType, Event]) => {
        e.preventDefault()
        history.push(s"?q=${searchText}")
      }

    div(
      h1("Search"),
      hr(),
      div(className := "row")(
        div(className := "col-5")(
          h4("Search Form"),
          hr(),
          form(onSubmit := handleSearch)(
            input(
              `type` := "text",
              placeholder := "Find your hero",
              className := "form-control",
              name := "searchText",
              autoComplete := "off",
              value := searchText,
              onChange := handleInputChange
            ),
            button(
              `type` := "submit",
              className := "btn mt-2 btn-block btn-outline-primary"
            )("Search")
          )
        ),
        div(className := "col-7")(
          h4("Results"),
          hr(),
          if (q.isEmpty || filteredHeroes.length <= 0) {
            div(className := "alert alert-info")("Search a hero")
          } else {
            filteredHeroes.map(hero => HeroCard(hero).withKey(hero.id))
          }
        )
      )
    )
  }
}
