package io.github.mvillafuertem.routers

import japgolly.scalajs.react.ScalaFnComponent
import typings.reactRouter.mod.{ RouteProps, RouterProps }
import japgolly.scalajs.react.vdom.html_<^.{ <, _ }
import typings.reactRouterDom.components.{ Redirect, Route, Switch, BrowserRouter => Router }
import io.github.mvillafuertem.components.journal.JournalScreen

object AppRouter {

  val component = ScalaFnComponent[Unit] { _ =>
    Router(
      <.div(
        Switch(
          Route(
            RouteProps()
              //.setExact(true)
              .setPath("/auth")
              .setRender(_ => AuthRouter.component().rawElement)
          ),
          Route(
            RouteProps()
              .setPath("/")
              .setRender(_ => JournalScreen.component().rawElement)
          ),
          Redirect("/auth/login")
        )
      )
    )
  }

}
