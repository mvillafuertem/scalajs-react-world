package io.github.mvillafuertem.router

import io.github.mvillafuertem.chat.domain.model.User
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.facade.React
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }
import org.scalajs.dom.console
import typings.reactRouter.components.{ Redirect, Route }
import typings.reactRouter.mod.RouteProps

// THIS DOESN'T WORK INSIDE APP ROUTER :(
object PrivateRoute {

  case class Props(user: Option[User], routeProps: RouteProps, element: React.Element)

  val component: Component[Props, CtorType.Props] = ScalaFnComponent[Props] { case Props(user, routeProps, element) =>
    Route(
      routeProps.setRender { _ =>
        console.log("redirect")

        user.fold({
          console.log("redirect")
          Redirect("/auth").rawElement
        }) { _ =>
          console.log("redirect")

          element
        }
      }
    )
  }

}
