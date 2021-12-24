package io.github.mvillafuertem.routers

import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import typings.reactRouter.components.{ Redirect, Route }
import typings.reactRouter.mod.RouteProps

@react object PrivateRoute {

  case class Props(isAuthenticated: Boolean, child: ReactElement, routeProps: RouteProps)
  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { case Props(isAuthenticated, child, routeProps) =>
    Route(
      routeProps.setRender(_ =>
        if (isAuthenticated) {
          child
        } else {
          Redirect("/login").build
        }
      )
    )
  }

}
