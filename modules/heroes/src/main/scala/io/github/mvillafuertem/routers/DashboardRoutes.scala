package io.github.mvillafuertem.routers

import io.github.mvillafuertem.components.dc.DcScreen
import io.github.mvillafuertem.components.search.SearchScreen
import io.github.mvillafuertem.components.heroes.HeroScreen
import io.github.mvillafuertem.components.marvel.MarvelScreen
import io.github.mvillafuertem.components.ui.NavBar
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Fragment
import slinky.web.html.{ className, div }
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{ Redirect, Route, Switch }

@react object DashboardRoutes {

  type Props = Unit

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { _ =>
    Fragment(
      NavBar(),
      div(className := "container mt-2")(
        Switch(
          Route(RouteProps().setExact(true).setPath("/marvel"))(MarvelScreen()),
          Route(RouteProps().setExact(true).setPath("/hero/:heroId").setRender(props => HeroScreen(props.history))),
          Route(RouteProps().setExact(true).setPath("/dc"))(DcScreen()),
          Route(RouteProps().setExact(true).setPath("/search").setRender(props => SearchScreen(props.history))),
          Redirect("/marvel")
        )
      )
    )
  }
}
