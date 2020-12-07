package io.github.mvillafuertem.router

import io.github.mvillafuertem.pages.ChatPage
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{ Redirect, Route, Switch, HashRouter => Router }

object AppRouter {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    Router(
      <.div(
        Switch(
          Route(RouteProps().setPath("/auth").setRender(_ => AuthRouter.component().rawElement)),
          Route(RouteProps().setExact(true).setPath("/").setRender(_ => ChatPage.component().rawElement)),
          Redirect("/")
        )
      )
    )
  }

}
