package io.github.mvillafuertem.router

import io.github.mvillafuertem.chat.domain.model.User
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.{ html_<^, VdomElement }
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ CtorType, PropsChildren, ScalaFnComponent }
import org.scalajs.dom.html.Element
import typings.reactRouter.mod.{ `match`, RouteProps }
import typings.reactRouterDom.components.{ Redirect, Route }
import typings.reactRouterDom.mod.useRouteMatch

object PublicRoute {

  case class Props(user: Option[User])

//  val JustChildren: Component[Unit, CtorType.Children] = ScalaFnComponent.justChildren((c: PropsChildren) => <.h4.apply(c))
//
//  val c1: VdomTagOf[Element] = <.i("good")
//
//  JustChildren.apply(c1)
  val component: Component[Props, CtorType.PropsAndChildren] = ScalaFnComponent.withChildren[Props] { case (p: Props, a) =>
    Route(RouteProps().setPath("/auth"))(p.user.fold(a.rawNode)(_ => Redirect("/").rawNode))
  }

}
