package io.github.mvillafuertem.routers

import japgolly.scalajs.react.ScalaFnComponent
import japgolly.scalajs.react.raw.React
import japgolly.scalajs.react.vdom.VdomElement
import typings.react.mod.ReactElement
import typings.reactRouter.components.{Redirect, Route}
import typings.reactRouter.mod.RouteProps

object PrivateRoute {

  case class Props(isAuthenticated: Boolean, child: React.Element, routeProps: RouteProps)
  val component = ScalaFnComponent[Props] { case Props(isAuthenticated, child, routeProps) =>
    Route(
      routeProps.setRender(_ =>
        if (isAuthenticated) {
          child
        } else {
          Redirect("/auth/login").build.rawElement
        }
      )
    )
  }

}
