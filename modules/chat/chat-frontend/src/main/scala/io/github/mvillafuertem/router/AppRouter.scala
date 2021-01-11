package io.github.mvillafuertem.router

import io.github.mvillafuertem.pages.{ ChatPage, LoginPage, RegisterPage }
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{ Redirect, Route, Switch, BrowserRouter => Router }

object AppRouter {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    Router(
      <.div(
        Switch(
          Route(RouteProps().setPath("/auth")).withKey("auth")(AuthRouter.component()),
          Route(RouteProps().setExact(true).setPath("/")).withKey("home")(ChatPage.component()),
          Redirect("/")
        )
      )
    )
  }

}
