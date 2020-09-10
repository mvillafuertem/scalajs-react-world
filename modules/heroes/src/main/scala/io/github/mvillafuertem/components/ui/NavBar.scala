package io.github.mvillafuertem.components.ui

import io.github.mvillafuertem.auth.AuthAction.Logout
import io.github.mvillafuertem.auth.authContext
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.{Fragment, Hooks}
import slinky.core.facade.Hooks.useContext
import slinky.web.html.{button, className, div, h1, hr, id, nav, onClick, p, span, ul}
import typings.reactRouterDom.components.{Link, NavLink}
import typings.reactRouterDom.mod.useHistory

@react object NavBar {

  type Props = Unit

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { _ =>
    val (user, dispatch) = useContext(authContext)
    val history = useHistory()
    val handleLogout     = () => {
      history.replace("/login")
      dispatch(Logout(user))
    }
    nav(className := "navbar navbar-expand-sm navbar-dark bg-dark")(
      Link[String](to = "/").className("navbar-brand")("HeroesApp"),
      div(className := "navbar-collapse")(
        div(className := "navbar-nav")(
          NavLink[String](to = "/marvel")
            .exact(true)
            .activeClassName("active")
            .className("nav-item nav-link")("Marvel"),
          NavLink[String](to = "/dc")
            .exact(true)
            .activeClassName("active")
            .className("nav-item nav-link")("DC"),
          NavLink[String](to = "/search")
            .exact(true)
            .activeClassName("active")
            .className("nav-item nav-link")("Search")
        )
      ),
      div(className := "navbar-collapse collapse w-100 order-3 dual-collapse2")(
        ul(className := "navbar-nav ml-auto")(
          span(className := "nav-item nav-link text-info")(s"Welcome ${user.name}!"),
          button(className := "nav-item nav-link btn", onClick := handleLogout)("Logout")
//          NavLink[String](to = "/login")
//            .exact(true)
//            .activeClassName("active")
//            .className("nav-item nav-link")("Login")
        )
      )
    )
  }
}
