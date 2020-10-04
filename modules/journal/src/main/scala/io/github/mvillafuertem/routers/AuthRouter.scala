package io.github.mvillafuertem.routers

import io.github.mvillafuertem.components.auth.{LoginScreen, RegisterScreen}
import japgolly.scalajs.react.ScalaFnComponent
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{Redirect, Route, Switch}

import scala.scalajs.js

object AuthRouter {

  val component = ScalaFnComponent[Unit] { _ =>
    <.div(^.className := "auth__main")(
      <.div(^.className := "auth__box-container")(
        Switch(
          Route(
            RouteProps()
              .setExact(true)
              .setPath("/auth/login")
              .setRender(_ => LoginScreen.component().rawElement)
          ),
          Route(
            RouteProps()
              .setExact(true)
              .setPath("/auth/register")
              .setRender(_ => RegisterScreen.component().rawElement)
          ),
          Redirect("/auth/login")
        )
      )
    )
  }

}
