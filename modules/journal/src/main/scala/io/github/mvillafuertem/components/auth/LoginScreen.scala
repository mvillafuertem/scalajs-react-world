package io.github.mvillafuertem.components.auth

import io.github.mvillafuertem.actions.{AppActions, AuthAction, UiAction}
import io.github.mvillafuertem.firebase.FirebaseConfiguration
import io.github.mvillafuertem.hooks.useForm
import io.github.mvillafuertem.model.Person
import io.github.mvillafuertem.reducers.AppState
import japgolly.scalajs.react.React.Fragment
import japgolly.scalajs.react.component.Js
import japgolly.scalajs.react.vdom.SvgTags.text
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{Callback, Children, CtorType, JsComponent, ReactEventFromInput, ScalaFnComponent}
import typings.firebase.mod.User
import typings.history.mod.{History, LocationState}
import typings.reactRedux.mod.{connect, useDispatch, useSelector}
import typings.reactRouterDom.components.{Link, Redirect}
import typings.reduxThunk.mod.ThunkDispatch
import typings.sweetalert2.mod.{SweetAlertIcon, default => Swal}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

object LoginScreen {

  val component = ScalaFnComponent[Unit] { _ =>
    val dispatch = useDispatch[ThunkDispatch[js.Any, js.Any, AppActions]]()
    val loading = useSelector[AppState, AppState](state => state.asInstanceOf[js.Dynamic].uiReducer.loading)
    val (state, handleInputChange) = useForm(Person.default)

    val handleLogin: js.Function1[ReactEventFromInput, Callback] =
      (e: ReactEventFromInput) =>
        Callback {
          dispatch(UiAction.StartLoading())
          (for {
            userCredential <- FirebaseConfiguration.firebase
              .auth()
              .signInWithEmailAndPassword(state.email, state.password)
              .toFuture
          } yield {
            dispatch(
              AuthAction.Login(
                Person(
                  userCredential.user.asInstanceOf[User].uid,
                  userCredential.user.asInstanceOf[User].displayName.asInstanceOf[String]
                )
              )
            )
            dispatch(UiAction.FinishLoading())
          }).recoverWith { case e: Exception =>
            println(e)
            Swal.fire("Error", e.getMessage, SweetAlertIcon.error).toFuture
          }
        } >> e.preventDefaultCB

    val handleGoogleLogin = () =>
      FirebaseConfiguration.firebase.auth().signInWithPopup(FirebaseConfiguration.googleAuthProvider).toFuture.map { userCredential =>
        dispatch(
          AuthAction.Login(
            Person(
              userCredential.user.asInstanceOf[User].uid,
              userCredential.user.asInstanceOf[User].displayName.asInstanceOf[String]
            )
          )
        )
      }

    Fragment(
      <.h3(^.className := "auth__title")("Login"),
      <.form(^.onSubmit ==> handleLogin)(
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
        <.button(
          ^.`type` := "submit",
          ^.className := "btn btn-primary btn-block",
          ^.disabled := loading.asInstanceOf[Boolean]
        )("Login"),
        <.div(^.className := "auth__social-networks")(
          <.p("Login with social networks"),
          <.div(^.className := "google-btn", ^.onClick --> Callback(handleGoogleLogin()))(
            <.div(^.className := "google-icon-wrapper")(
              <.img(^.className := "google-icon", ^.src := "https://upload.wikimedia.org/wikipedia/commons/5/53/Google_%22G%22_Logo.svg")),
            <.p(^.className := "btn-text")(
              <.b("Sign in with google")
            )
          )
        ),
        Link[String]("/auth/register").className("link")("Create new account")
      )
    )

  }

}
