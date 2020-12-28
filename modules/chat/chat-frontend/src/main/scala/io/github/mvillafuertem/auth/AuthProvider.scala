package io.github.mvillafuertem.auth

import io.github.mvillafuertem.auth
import io.github.mvillafuertem.chat.domain.model.User
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.raw.React.Element
import japgolly.scalajs.react.vdom.{VdomElement, VdomNode}
import japgolly.scalajs.react.{CtorType, React, ScalaFnComponent}
import typings.react.mod._

import scala.scalajs.js

object AuthProvider {

  val ctx = React.createContext()

  val component: Component[VdomNode, CtorType.Props] = ScalaFnComponent[VdomNode] { children =>
    val auth, setAuth = useState[User](User("", "", ""))

    val login: js.Function2[String, String, Unit] = (email, password) => ()

    val register: js.Function3[String, String, String, Unit] = (name, email, password) => ()

    val verifyToken: Unit = useCallback[Unit](() => (), js.Array[js.Any]())

    val logout: js.Function0[Unit] = () => ()

    ctx.provide((login, register, verifyToken, logout))(children)
  }

}
