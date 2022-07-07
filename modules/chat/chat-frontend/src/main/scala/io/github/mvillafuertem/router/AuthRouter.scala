package io.github.mvillafuertem.router

import io.github.mvillafuertem.pages.{ LoginPage, RegisterPage }
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{ <, _ }
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }
import typings.reactRouter.mod.{ `match`, RouteProps }
import typings.reactRouterDom.components.{ Redirect, Route, Switch }
import typings.reactRouterDom.mod.useRouteMatch

import scala.scalajs.js.|

object AuthRouter {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    val routeMatch: `match`[String] = useRouteMatch[String]().asInstanceOf[`match`[String]]
    <.div(^.className := "limiter")(
      <.div(^.className := "container-login100")(
        <.div(^.className := "wrap-login100 p-t-50 p-b-90")(
          Switch(
            Route(RouteProps().setExact(true).setPath(s"${routeMatch.path}/login")).withKey("login")(LoginPage.component()),
            Route(RouteProps().setExact(true).setPath(s"${routeMatch.path}/register")).withKey("register")(RegisterPage.component()),
            Redirect(s"${routeMatch.path}/login")
          )
        )
      )
    )
  }

}
