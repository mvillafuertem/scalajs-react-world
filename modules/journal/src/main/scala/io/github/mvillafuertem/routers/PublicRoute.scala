package io.github.mvillafuertem.routers

import io.github.mvillafuertem.components.auth.LoginScreen
import io.github.mvillafuertem.routers.PrivateRoute.Props
import japgolly.scalajs.react.ScalaFnComponent
import typings.history.mod.{History, LocationState}
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{Redirect, Route}

import scala.scalajs.js

object PublicRoute {

  case class Props(isAuthenticated: Boolean, routeProps: RouteProps)


  val component = ScalaFnComponent[Props] { case Props(isAuthenticated, routeProps) => {
    Route(
      routeProps.setRender(_ =>
        if (isAuthenticated) {
          Redirect("/").build.rawElement
        } else {
          AuthRouter.component().rawElement
        }
      )
    )
  }
  }

}
