package io.github.mvillafuertem

import io.github.mvillafuertem.AuthProvider.{ withAuthProvider, AuthComponentProps }
import io.github.mvillafuertem.ErrorMessage.ErrorMessageProps
import io.github.mvillafuertem.NavBar.NavBarProps
import io.github.mvillafuertem.Welcome.WelcomeProps
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.document
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{ HashRouter => Router, Redirect, Route }
import typings.reactstrap.components.Container
import zio.{ App, ExitCode, IO, ZIO }

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object CalendarApp extends App {

  @JSImport("./index.css", JSImport.Namespace)
  @js.native
  object CalendarCSS extends js.Object

  @JSImport("bootstrap/dist/css/bootstrap.min.css", JSImport.Namespace)
  @js.native
  object BootstrapMinCSS extends js.Object

  @JSImport("@fortawesome/fontawesome-free/css/all.css", JSImport.Namespace)
  @js.native
  object FontAwesomeCSS extends js.Object

  object App {

    val component: Component[AuthComponentProps, CtorType.Props] = ScalaFnComponent[AuthComponentProps] { props =>
      Router(
        <.div(
          NavBar.component(NavBarProps(props.isAuthenticated, _ => props.logout(), props.user)).when(props.isAuthenticated),
          NavBar.component(NavBarProps(props.isAuthenticated, _ => props.login(), props.user)).when(!props.isAuthenticated),
          Container()(
            ErrorMessage.component(props.error).when(props.error != null && props.error.message.nonEmpty),
            Route(
              RouteProps()
                .setExact(true)
                .setPath("/")
                .setRender(_ => Welcome.component(WelcomeProps(props.isAuthenticated, _ => props.login(), props.user)).rawElement)
            ),
            Route(
              RouteProps()
                .setExact(true)
                .setPath("/calendar")
                .setRender(_ => if (props.isAuthenticated) <.div("Calendar").rawElement else Redirect("/").rawElement)
            )
          ).build
        )
      )
    }
  }

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    IO.effectTotal {
      CalendarCSS
      BootstrapMinCSS
      FontAwesomeCSS
      withAuthProvider(App.component.ctor)().renderIntoDOM(document.getElementById("container"))
    }.exitCode

}
