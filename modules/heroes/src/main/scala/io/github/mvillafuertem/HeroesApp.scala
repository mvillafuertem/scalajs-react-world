package io.github.mvillafuertem

import io.github.mvillafuertem.auth.AuthAction.Login
import io.github.mvillafuertem.auth.{ initialUser, AuthAction, AuthReducer }
import io.github.mvillafuertem.model.User
import io.github.mvillafuertem.routers.AppRouter
import org.scalajs.dom.document
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Hooks.{ useEffect, useReducer }
import slinky.web.ReactDOM
import zio.{ App, ExitCode, IO, ZIO }

object HeroesApp extends App {

  @react object Main {

    type Props = Unit

    val component: FunctionalComponent[Props] = FunctionalComponent[Props] { _ =>
      val (state, dispatch) = useReducer[User, User, AuthAction](AuthReducer.apply, initialUser, _ => initialUser)
      auth.authContext.Provider((state, dispatch))(
        AppRouter()
      )
    }
  }

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    IO.effectTotal {
      ReactDOM
        .render(
          Main(),
          document.getElementById("container")
        )
    }.exitCode

}
