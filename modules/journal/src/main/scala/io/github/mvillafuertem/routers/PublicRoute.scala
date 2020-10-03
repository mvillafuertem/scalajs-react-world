package io.github.mvillafuertem.routers

import japgolly.scalajs.react.ScalaFnComponent
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{Redirect, Route}

import scala.scalajs.js

object PublicRoute {

  case class Props(isAuthenticated: Boolean, routeProps: RouteProps)

  val component = ScalaFnComponent[Props] { case Props(isAuthenticated, routeProps) =>
    println("asdfasdf asdf  asd f" +  js.JSON.stringify(routeProps))
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
