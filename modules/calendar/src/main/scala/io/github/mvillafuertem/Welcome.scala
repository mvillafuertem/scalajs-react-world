package io.github.mvillafuertem

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{Callback, CtorType, ReactMouseEventFrom, ScalaFnComponent}
import org.scalajs.dom.raw.Element
import typings.reactstrap.components.{Button, Jumbotron}

import scala.scalajs.js

object Welcome {

  case class WelcomeProps(isAuthenticated: Boolean, authButtonMethod: ReactMouseEventFrom[js.Any with Element] => Callback, user: js.Any)

  val welcomeContent: js.Function1[WelcomeProps, TagMod] = props =>
    // If authenticated, greet the user
    if (props.isAuthenticated) {
      <.div(
        <.h4(s"Welcome ${props.user}!"),
        <.p("Use the navigation bar at the top of the page to get started.")
      )
    } else { // Not authenticated, present a sign in button
      Button().color("primary").onClick(props.authButtonMethod)
    }

  val component: Component[WelcomeProps, CtorType.Props] = ScalaFnComponent[WelcomeProps] { props =>
    Jumbotron()(
      <.h1("React Graph Tutorial"),
      <.p(^.className := "lead")("This sample app shows how to use the Microsoft Graph API to access Outlook and OneDrive data from React"),
      welcomeContent(props)
    )
  }

}
