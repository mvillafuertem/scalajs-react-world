package io.github.mvillafuertem.auth

import io.github.mvillafuertem.hooks.useForm
import io.github.mvillafuertem.reducers.AuthReducer.Login
import japgolly.scalajs.react.React.Fragment
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.SvgTags.text
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{Callback, CtorType, ReactEventFromInput, ScalaFnComponent}
import typings.reactRedux.mod.useDispatch
import typings.redux.mod.{Action, AnyAction}

import scala.scalajs.js

object LoginScreen {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    val dispatch: Action[js.Any] = useDispatch[Action[js.Any]]()

    val (state, handleInputChange) = useForm()

    val handleSubmit: js.Function1[ReactEventFromInput, Callback] =
      (e: ReactEventFromInput) => {
        e.preventDefault()
        Callback {
          println(js.JSON.stringify(state))
          dispatch.setType(new Login)
        }
      }

    Fragment(
      <.h3(^.className := "auth__title")("Login"),
      <.form(^.onSubmit ==> handleSubmit)(
        <.input(
          ^.`type` := text.name,
          ^.placeholder := "Email",
          ^.name := "email",
          ^.className := "auth__input",
          ^.autoComplete := "off",
          ^.value := state.email,
          ^.onChange ==> handleInputChange
        ),
        <.input(
          ^.`type` := "password",
          ^.placeholder := "Password",
          ^.name := "password",
          ^.className := "auth__input",
          ^.autoComplete := "off",
          ^.value := state.password,
          ^.onChange ==> handleInputChange
        ),
        <.input(
          ^.`type` := "submit",
          ^.className := "btn btn-primary btn-block"
        )
      )
    )
  }

}
