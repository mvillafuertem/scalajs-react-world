package io.github.mvillafuertem.routers

import io.github.mvillafuertem.actions.{AppActions, AuthAction}
import io.github.mvillafuertem.components.journal.JournalScreen
import io.github.mvillafuertem.firebase.FirebaseConfiguration
import io.github.mvillafuertem.model.Person
import io.github.mvillafuertem.reducers.AppState
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{Callback, ScalaFnComponent}
import typings.firebase.mod.{Observer, User}
import typings.react.mod.{EffectCallback, useEffect, useState}
import typings.reactRedux.mod.useDispatch
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{Redirect, Route, Switch, HashRouter => Router}
import typings.reduxThunk.mod.{ThunkAction, ThunkDispatch}
import io.github.mvillafuertem.store.thunkDispatch

import scala.scalajs.js
import scala.scalajs.js.|

object AppRouter {

  val component = ScalaFnComponent[Unit] { _ =>
    val dispatch                             = useDispatch[ThunkAction[AppActions, AppState, js.Any, AppActions]]()
    val js.Tuple2(checking, setChecking)     = useState[Boolean](true)
    val js.Tuple2(isLoggedIn, setIsLoggedIn) = useState[Boolean](false)
    val effect: js.Function0[Unit] =
      () =>
        FirebaseConfiguration.firebase
          .auth()
          .onAuthStateChanged(
            js.Any
              .fromFunction1((user: User) =>
                Option(user) match {
                  case Some(value) =>
                    (for {
                      _ <- Callback(
                        dispatch(
                          thunkDispatch(
                            AuthAction.Login(
                              Person(
                                value.uid,
                                value.displayName.asInstanceOf[String]
                              )
                            )
                          )
                        )
                      )
                      _ <- Callback(setIsLoggedIn(true))
                      _ <- Callback(setChecking(false))
                    } yield ()).runNow()
                  case None => setIsLoggedIn(false)
                }
              )
              .asInstanceOf[js.Function1[User | Null, Any]]
          )

    useEffect(
      effect,
      js.Array[Any](dispatch, setChecking, setIsLoggedIn)
    )

    if (checking) {
      <.h1("Wait...")
    }

    <.div(
      Router(
        <.div(
          Switch(
            Route(
              RouteProps()
                .setExact(true)
                .setPath("/")
                .setRender(_ =>
                  if (isLoggedIn) {
                    JournalScreen.component().rawElement
                  } else {
                    Redirect("/auth/login").rawElement
                  }
                )
            ),
            Route(
              RouteProps()
                .setPath("/auth")
                .setRender(_ =>
                  if (isLoggedIn) {
                    Redirect("/").rawElement
                  } else {
                    AuthRouter.component().rawElement
                  }
                )
            ),
            Redirect("/auth/login")
          )
        )
      )
    )
  }

}
