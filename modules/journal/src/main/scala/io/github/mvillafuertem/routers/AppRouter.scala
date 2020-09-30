package io.github.mvillafuertem.routers

import io.github.mvillafuertem.components.auth.LoginScreen
import io.github.mvillafuertem.components.journal.JournalScreen
import japgolly.scalajs.react.ScalaFnComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{Redirect, Route, Switch, BrowserRouter => Router}

import scala.scalajs.js

object AppRouter {

  val component = ScalaFnComponent[Unit] { _ =>
    <.div(
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
    )
  }

}
