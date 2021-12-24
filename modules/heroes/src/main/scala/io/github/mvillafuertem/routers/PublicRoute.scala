package io.github.mvillafuertem.routers

import io.github.mvillafuertem.components.login.LoginScreen
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{ Redirect, Route }

@react object PublicRoute {

  case class Props(isAuthenticated: Boolean, routeProps: RouteProps)

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { case Props(isAuthenticated, routeProps) =>
    Route(
      routeProps.setRender(props =>
        if (!isAuthenticated) {
          LoginScreen(props.history)
        } else {
          Redirect("/").build
        }
      )
    )
  }

}
