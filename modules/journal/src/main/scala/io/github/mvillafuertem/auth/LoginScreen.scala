package io.github.mvillafuertem.auth

import io.github.mvillafuertem.ReduxFacade.Connected
import io.github.mvillafuertem.firebase.FirebaseConfiguration
import io.github.mvillafuertem.hooks.{ useForm, Person }
import io.github.mvillafuertem.reducers.AuthAction.Login
import io.github.mvillafuertem.reducers.{ AuthAction, AuthState }
import japgolly.scalajs.react.React.Fragment
import japgolly.scalajs.react.vdom.SvgTags.text
import japgolly.scalajs.react.vdom.html_<^.{ <, _ }
import japgolly.scalajs.react.{ Callback, ReactEventFromInput, ScalaFnComponent }
import typings.firebase.mod.User
import typings.redux.mod.Dispatch

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

object LoginScreen {

  @js.native
  trait Props extends js.Object {
    var state: AuthState = js.native
    var dispatch: Dispatch[AuthAction]
  }

  val component = ScalaFnComponent[Connected[AuthState, AuthAction] with Props] { props =>
    val (state, handleInputChange) = useForm(props.state.person)

    println("MYCOMPONENT" + js.JSON.stringify(props))

    val handleSubmit: js.Function1[ReactEventFromInput, Callback] =
      (e: ReactEventFromInput) =>
        Callback {
          FirebaseConfiguration.firebase.auth().signInWithPopup(FirebaseConfiguration.googleAuthProvider).toFuture.map { userCredential =>
            props.dispatch(
              Login(
                Person(
                  userCredential.user.asInstanceOf[User].uid,
                  userCredential.user.asInstanceOf[User].displayName.asInstanceOf[String]
                )
              )
            )
          }
        } >> e.preventDefaultCB

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
