package io.github.mvillafuertem.router

import io.github.mvillafuertem.pages.{ LoginPage, RegisterPage }
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{ <, _ }
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{ Redirect, Route, Switch }

object AuthRouter {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    <.div(^.className := "limiter")(
      <.div(^.className := "container-login100")(
        <.div(^.className := "wrap-login100 p-t-50 p-b-90")(
          Switch(
            Route(RouteProps().setExact(true).setPath("/auth/login").setRender(_ => LoginPage.component().rawElement)),
            Route(RouteProps().setExact(true).setPath("/auth/register").setRender(_ => RegisterPage.component().rawElement)),
            Redirect("/auth/login")
          )
        )
      )
    )
  }

}
