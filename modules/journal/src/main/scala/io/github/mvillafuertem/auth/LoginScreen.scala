package io.github.mvillafuertem.auth

import io.github.mvillafuertem.actions.{ AppActions, AuthAction }
import io.github.mvillafuertem.firebase.FirebaseConfiguration
import io.github.mvillafuertem.hooks.{ useForm, Person }
import io.github.mvillafuertem.reducers.AppState
import io.github.mvillafuertem.states.AuthState
import japgolly.scalajs.react.React.Fragment
import japgolly.scalajs.react.component.Js
import japgolly.scalajs.react.vdom.SvgTags.text
import japgolly.scalajs.react.vdom.html_<^.{ <, _ }
import japgolly.scalajs.react.{ Callback, Children, CtorType, JsComponent, ReactEventFromInput, ScalaFnComponent }
import typings.firebase.mod.User
import typings.reactRedux.mod.connect
import typings.redux.mod.Dispatch

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

object LoginScreen {

  @js.native
  trait Props extends js.Object {
    var state: AuthState = js.native
    var dispatch: Dispatch[AuthAction]
  }

  val component = ScalaFnComponent[Props] { props =>
    val (state, handleInputChange) = useForm(props.state.person)

    println("MYCOMPONENT" + js.JSON.stringify(props))

    val handleSubmit: js.Function1[ReactEventFromInput, Callback] =
      (e: ReactEventFromInput) =>
        Callback {
          FirebaseConfiguration.firebase.auth().signInWithPopup(FirebaseConfiguration.googleAuthProvider).toFuture.map { userCredential =>
            props.dispatch(
              AuthAction.Login(
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

  val mapStateToProps: js.Function1[AppState, js.Dynamic] =
    (state: AppState) => js.Dynamic.literal(state = state.asInstanceOf[js.Dynamic].authReducer)

  val mapDispatchToProps: js.Function1[Dispatch[AppActions], js.Dynamic] =
    (dispatch: Dispatch[AppActions]) => js.Dynamic.literal(dispatch = dispatch)

  val connectElem: Js.Component[LoginScreen.Props, Null, CtorType.PropsAndChildren] =
    JsComponent[LoginScreen.Props, Children.Varargs, Null](
      connect.asInstanceOf[js.Dynamic](mapStateToProps, mapDispatchToProps)(this.component.toJsComponent.raw)
    )

}
