package io.github.mvillafuertem.routers

import io.github.mvillafuertem.actions.{AppActions, AuthAction}
import io.github.mvillafuertem.components.auth.LoginScreen
import io.github.mvillafuertem.components.journal.JournalScreen
import io.github.mvillafuertem.firebase.FirebaseConfiguration
import io.github.mvillafuertem.model.Person
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.{Callback, ScalaFnComponent}
import typings.firebase.mod.{Observer, User}
import typings.react.mod.{EffectCallback, useEffect, useState}
import typings.reactRedux.mod.useDispatch
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{Redirect, Route, Switch, BrowserRouter => Router}
import typings.reduxThunk.mod.ThunkDispatch

import scala.scalajs.js

object AppRouter {


  val component = ScalaFnComponent[Unit] { _ =>
    val dispatch = useDispatch[ThunkDispatch[js.Any, js.Any, AppActions]]()
    val js.Tuple2(checking, setChecking) = useState[Boolean](false)
    val js.Tuple2(isLoggedIn, setIsLoggedIn) = useState[Boolean](false)
    val observerUser: Observer[User, js.Error] = js.Dynamic.literal(next = js.Any.fromFunction1 {
      (user: User) =>
        (Callback(
          if (user != null) {
            dispatch(
              AuthAction.Login(
                Person(
                  user.uid,
                  user.displayName.asInstanceOf[String]
                )
              )
            )
            setIsLoggedIn(true)
          } else setIsLoggedIn(false)
        ) >> Callback(setChecking(false))).runNow()

    }).asInstanceOf[Observer[User, js.Error]]

    useEffect((() =>
      FirebaseConfiguration
        .firebase
        .auth()
        .onAuthStateChanged(observerUser)): EffectCallback,
      js.Array[js.Any](dispatch, setChecking, setIsLoggedIn)
    )

    <.div(
      Router(
        <.div(
          Switch(
            //Route(RouteProps().setExact(true).setPath("/login").setRender(props => LoginScreen(props.history))),
            //PublicRoute(state.logged, LoginScreen.component, RouteProps().setExact(true).setPath("/login")),
            // Esto debería ser PublicRoute, pero no funciona el redireccionamiento cuando está dentro de PublicRoute
            Route(
              RouteProps()
                .setExact(true)
                .setPath("/auth/*")
                .setRender(_ =>
                  if (isLoggedIn) {
                    Redirect("/").rawElement
                  } else {
                    AuthRouter.component().rawElement
                  }
                )
            ),
            PrivateRoute.component(PrivateRoute.Props(isLoggedIn)),
            Redirect("/auth/login")
          )
        )
      )
    )
  }

}
