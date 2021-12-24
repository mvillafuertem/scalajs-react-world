package io.github.mvillafuertem.components.login

import io.github.mvillafuertem.auth.AuthAction.Login
import io.github.mvillafuertem.auth.authContext
import io.github.mvillafuertem.model.User
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Hooks
import slinky.web.html._
import typings.history.mod.{ History, LocationState }

@react object LoginScreen {

  case class Props(history: History[LocationState])

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { case Props(history) =>
    val (state, dispatch) = Hooks.useContext(authContext)

    val handleLogin = () => {
      // history.push("/")
      dispatch(Login(User("Pepe", logged = true)))
      history.replace("/")
    }

    div(className := "container mt-5")(
      h1("LoginScreen"),
      hr(),
      button(className := "btn btn-primary", onClick := handleLogin)("Login")
    )
  }
}
