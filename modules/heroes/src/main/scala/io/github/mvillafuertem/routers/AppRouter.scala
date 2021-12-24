package io.github.mvillafuertem.routers

import io.github.mvillafuertem.auth.authContext
import io.github.mvillafuertem.components.login.LoginScreen
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Hooks
import slinky.web.html.div
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{ HashRouter => Router, Redirect, Route, Switch }

@react object AppRouter {

  type Props = Unit

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { _ =>
    val (state, _) = Hooks.useContext(authContext)

    Router(
      div(
        Switch(
          // Route(RouteProps().setExact(true).setPath("/login").setRender(props => LoginScreen(props.history))),
          // PublicRoute(state.logged, LoginScreen.component, RouteProps().setExact(true).setPath("/login")),
          // Esto debería ser PublicRoute, pero no funciona el redireccionamiento cuando está dentro de PublicRoute
          Route(
            RouteProps()
              .setExact(true)
              .setPath("/login")
              .setRender(props =>
                if (!state.logged) {
                  LoginScreen(props.history)
                } else {
                  Redirect("/").build
                }
              )
          ),
          PrivateRoute(state.logged, DashboardRoutes(), RouteProps().setPath("/"))
        )
      )
    )
  }
}
