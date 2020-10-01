package io.github.mvillafuertem.components.journal

import io.github.mvillafuertem.actions.AppActions
import io.github.mvillafuertem.actions.AuthAction.{Login, Logout}
import io.github.mvillafuertem.firebase.FirebaseConfiguration
import japgolly.scalajs.react.{Callback, ScalaFnComponent}
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import typings.reactRedux.mod.useDispatch
import typings.reduxThunk.mod.ThunkDispatch

import scala.scalajs.js

object Sidebar {

  val component = ScalaFnComponent[Unit] { _ =>
    val dispatch = useDispatch[ThunkDispatch[js.Any, js.Any, AppActions]]()
    val handleLogout = () =>
      Callback(FirebaseConfiguration.firebase.auth().signOut()) >>
        Callback(dispatch(Logout()))

    <.aside(^.className := "journal__sidebar")(
      <.div(^.className := "journal__sidebar-navbar")(
        <.h3(^.className := "mt-5")(
          <.i(^.className := "far fa-moon"),
          <.span(" Pepe")
        ),
        <.button(^.className := "btn", ^.onClick --> handleLogout())("Logout")
      ),
      <.div(^.className := "journal__new-entry")(
        <.i(^.className := "far fa-calendar-plus fa-5x"),
        <.p("New entry")
      ),
      JournalEntries.component()
    )
  }

}
