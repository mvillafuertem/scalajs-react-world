package io.github.mvillafuertem

import io.github.mvillafuertem.ErrorMessage.ErrorMessageProps
import io.github.mvillafuertem.NavBar.NavBarProps
import io.github.mvillafuertem.Welcome.WelcomeProps
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.{CtorType, ScalaFnComponent}
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.document
import typings.reactRouterDom.components.{Route, BrowserRouter => Router}
import typings.reactRouterDom.mod.Route
import typings.reactstrap.components.Container
import zio.{App, ExitCode, IO, ZIO}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object CalendarApp extends App {

  @JSImport("./index.css", JSImport.Namespace)
  @js.native
  object CalendarCSS extends js.Object

  object App {
    val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { props =>

      Router(
        <.div(
          Container()(
            ErrorMessage.component(ErrorMessageProps("", "")).when(true),
            Route.set("path", "/")(
            )
          )
        )
      )
    }
  }

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    IO.effectTotal {
      CalendarCSS
      App.component().renderIntoDOM(document.getElementById("container"))
    }.exitCode

}
