package io.github.mvillafuertem

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ Callback, CtorType, ReactMouseEventFrom, ScalaFnComponent }
import org.scalablytyped.runtime.StringDictionary
import org.scalajs.dom.Element
import typings.reactstrap.components.Button

import scala.scalajs.js

object Welcome {

  case class WelcomeProps(isAuthenticated: Boolean, authButtonMethod: js.Function1[ReactMouseEventFrom[js.Any with Element], Callback], user: User)

  val welcomeContent: js.Function1[WelcomeProps, TagMod] = props =>
    // If authenticated, greet the user
    if (props.isAuthenticated) {
      <.div(
        <.h4(s"Welcome ${props.user.displayName}!"),
        <.p("Use the navigation bar at the top of the page to get started.")
      )
    } else { // Not authenticated, present a sign in button
      Button().color("primary").onClick(props.authButtonMethod)("Click here to sign in").build
    }

  val component: Component[WelcomeProps, CtorType.Props] = ScalaFnComponent[WelcomeProps] { props =>
    <.div(
      ^.style := StringDictionary(
        "padding"          -> "4rem 2rem",
        "margin-bottom"    -> "2rem",
        "background-color" -> "#F8F9FA",
        "border-radius"    -> ".3rem"
      )
    )(
      <.h1("React Graph Tutorial"),
      <.p(^.className := "lead")("This sample app shows how to use the Microsoft Graph API to access Outlook and OneDrive data from React"),
      welcomeContent(props)
    )
  }

}
