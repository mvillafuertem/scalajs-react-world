package io.github.mvillafuertem.routers

import io.github.mvillafuertem.components.auth.LoginScreen
import io.github.mvillafuertem.components.auth.RegisterScreen
import japgolly.scalajs.react.ScalaFnComponent
import japgolly.scalajs.react.vdom.html_<^.<
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{ Route, Switch, Redirect }

import scala.scalajs.js

object AuthRouter {

  val component = ScalaFnComponent[Unit] { _ =>
    <.div(
      Switch(
        Route(
          RouteProps()
            .setExact(true)
            .setPath("/auth/login")
            .setRender(_ => LoginScreen.connectElem((new js.Object).asInstanceOf[LoginScreen.Props])().rawElement)
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
  }

}
