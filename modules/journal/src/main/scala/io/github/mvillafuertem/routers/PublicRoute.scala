package io.github.mvillafuertem.routers

import japgolly.scalajs.react.ScalaFnComponent
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{ Redirect, Route }

import scala.scalajs.js

object PublicRoute {

  case class Props(isLoggedIn: Boolean)

  val component = ScalaFnComponent[Props] { case Props(isLoggedIn) =>
    Route(
      RouteProps()
        .setExact(true)
        .setPath("/auth/login")
        .setRender(_ =>
          if (isLoggedIn) {
            Redirect("/").rawElement
          } else {
            AuthRouter.component().rawElement
          }
        )
    )
  }

}
