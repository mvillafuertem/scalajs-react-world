package io.github.mvillafuertem.routers

import io.github.mvillafuertem.components.journal.JournalScreen
import japgolly.scalajs.react.ScalaFnComponent
import japgolly.scalajs.react.raw.React
import japgolly.scalajs.react.vdom.VdomElement
import typings.react.mod.ReactElement
import typings.reactRouter.components.{Redirect, Route}
import typings.reactRouter.mod.RouteProps

object PrivateRoute {

  case class Props(isAuthenticated: Boolean)
  val component = ScalaFnComponent[Props] { case Props(isAuthenticated) =>
    Route(
      RouteProps()
        .setPath("/").setRender(_ =>
        if (isAuthenticated) {
          JournalScreen.component().rawElement
        } else {
          Redirect("/auth/login").build.rawElement
        }
      )
    )
  }

}
