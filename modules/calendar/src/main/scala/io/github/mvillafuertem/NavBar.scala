package io.github.mvillafuertem

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ Callback, CtorType, ReactMouseEventFrom, ScalaFnComponent }
import org.scalablytyped.runtime.StringDictionary
import org.scalajs.dom.raw.{ Element, HTMLAnchorElement }
import typings.react.mod.useState
import typings.reactstrap.components._
import typings.reactRouterDom.components.{ NavLink => RouterNavLink }

import scala.scalajs.js

object NavBar {

  case class NavBarProps(isAuthenticated: Boolean, authButtonMethod: ReactMouseEventFrom[js.Any with Element] => Callback, user: User)

  val userAvatar: js.Function1[User, TagMod] = user => {
    if (user.avatar.nonEmpty) {
      <.img(
        ^.src := "",
        ^.alt := "user",
        ^.className := "rounded-circle align-self-center mr-2",
        ^.style := StringDictionary("width" -> "32px")
      )
    } else
      <.i(
        ^.className := "far fa-user-circle fa-lg rounded-circle align-self-center mr-2",
        ^.style := StringDictionary("width" -> "32px")
      )
  }

  val authNavItem: js.Function1[NavBarProps, TagMod] = props => {
    if (props.isAuthenticated) {
      UncontrolledDropdown()(
        DropdownToggle()
          .nav(true)
          .caret(true)(
            userAvatar(props.user)
          ),
        DropdownMenu.right(true)(
          <.h5(^.className := "dropdown-item-text mb-0")(props.user.displayName),
          <.p(^.className := "dropdown-item-text text-muted mb-0")(props.user.email),
          DropdownItem().divider(true),
          DropdownItem().onClick(props.authButtonMethod)
        )
      )
    } else {
      NavItem()(
        Button()
          .onClick(props.authButtonMethod)
          .className("btn-link nav-link border-0")
          .color("link")("Sign In")
      )
    }

  }

  val component: Component[NavBarProps, CtorType.Props] = ScalaFnComponent[NavBarProps] { props =>
    val js.Tuple2(isOpen, setOpen) = useState[Boolean](false)

    val toggle: js.Function1[ReactMouseEventFrom[HTMLAnchorElement], Callback] = _ => Callback(setOpen(!isOpen))

    Navbar()
      .color("dark")
      .expand("md")
      .dark(true)
      .fixed("top")(
        Container()(
          NavbarBrand().href("/")("React Graph Tutorial"),
          NavbarToggler().onClick(toggle),
          Collapse()
            .isOpen(isOpen)
            .navbar(true)(
              Nav()
                .className("mr-auto")
                .navbar(true)(
                  NavItem()(
                    RouterNavLink[String]("/").className("nav-link").exact(true)("Home")
                  ),
                  NavItem()(
                    RouterNavLink[String]("/calendar").className("nav-link").exact(true)("Calendar")
                  ).when(props.isAuthenticated)
                ),
              Nav()
                .className("justify-content-end")
                .navbar(true)(
                  NavItem()(
                    RouterNavLink[String]("/")
                      .href("https://developer.microsoft.com/graph/docs/concepts/overview")
                      .target("_target")
                      .className("nav-link")
                      .exact(true)(<.i(^.className := "fas fa-external-link-alt mr-1"), "Docs")
                  ),
                  authNavItem(props)
                )
            )
        )
      )

  }

}
