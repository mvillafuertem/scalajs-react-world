package io.github.mvillafuertem.components

import io.github.mvillafuertem.auth.{ AuthContext, ChatState }
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{ <, ^, _ }
import japgolly.scalajs.react.{ Callback, CtorType, ScalaFnComponent }
import typings.react.mod.useContext

object SearchBox {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    val ChatState(user, _, _, _, logout) = useContext(AuthContext.value)

    <.div(^.className := "headind_srch")(
      <.div(^.className := "recent_heading mt-2")(
        user.map(u => <.h4(s"${u.name}"))
      ),
      <.div(^.className := "srch_bar")(
        <.div(^.className := "stylish-input-group")(
          <.button(^.className := "btn text-danger", ^.onClick --> Callback(logout.get()))(
            "Salir"
          )
        )
      )
    )
  }

}
