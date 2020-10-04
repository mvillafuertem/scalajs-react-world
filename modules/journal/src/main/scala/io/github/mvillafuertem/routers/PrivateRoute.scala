package io.github.mvillafuertem.routers

import io.github.mvillafuertem.components.journal.JournalScreen
import japgolly.scalajs.react.ScalaFnComponent
import typings.reactRouter.components.{ Redirect, Route }
import typings.reactRouter.mod.RouteProps

// THIS DOESN'T WORK INSIDE APP ROUTER :(
object PrivateRoute {

  case class Props(isAuthenticated: Boolean, routeProps: RouteProps)

  val component = ScalaFnComponent[Props] { case Props(isAuthenticated, routeProps) =>
    Route(
      routeProps
        .setRender(_ =>
          if (isAuthenticated) {
            JournalScreen.component().rawElement
          } else {
            Redirect("/auth/login").rawElement
          }
        )
    )
  }

}
