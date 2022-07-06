package io.github.mvillafuertem.router

import io.github.mvillafuertem.auth.AuthContext
import io.github.mvillafuertem.pages.ChatPage
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }
import typings.react.mod.{ useContext, useEffect, DependencyList, EffectCallback }
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{ BrowserRouter => Router, Redirect, Route, Switch }

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

object AppRouter {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    val chatState = useContext(AuthContext.value)

    chatState.verifyToken
      .fold(throw new RuntimeException("verifytoken"))(function =>
        useEffect(
          (() => function(())):                             EffectCallback,
          js.Array[Any](chatState.verifyToken.orUndefined): DependencyList
        )
      )

    Router(
      <.div(
        Switch(
//          PublicRoute.component(
//            PublicRoute.Props(
//              chatState.user
//            )
//          )(AuthRouter.component()),
          Route(
            RouteProps()
              .setPath("/auth")
          )(chatState.user.fold(AuthRouter.component().vdomElement)(_ => Redirect("/").build)),
          Route(
            RouteProps()
              .setExact(true)
              .setPath("/")
          )(chatState.user.fold(Redirect("/auth").build)(_ => ChatPage.component().vdomElement)),
          Redirect("/")
        )
      )
    ).build

  }
}
