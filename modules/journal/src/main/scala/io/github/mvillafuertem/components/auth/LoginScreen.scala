package io.github.mvillafuertem.components.auth

import io.github.mvillafuertem.actions.{ AppActions, AuthAction, UiAction }
import io.github.mvillafuertem.firebase.FirebaseConfiguration
import io.github.mvillafuertem.hooks.useForm
import io.github.mvillafuertem.model.Person
import io.github.mvillafuertem.reducers.AppState
import japgolly.scalajs.react.React.Fragment
import japgolly.scalajs.react.vdom.SvgTags.text
import japgolly.scalajs.react.vdom.html_<^.{ <, _ }
import japgolly.scalajs.react.{ Callback, ReactEventFromInput, ScalaFnComponent }
import typings.firebase.mod.User
import typings.reactRedux.mod.{ useDispatch, useSelector }
import typings.reactRouterDom.components.Link
import typings.reduxThunk.mod.ThunkAction
import typings.sweetalert2.mod.{ default => Swal, SweetAlertIcon }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import io.github.mvillafuertem.store.thunkDispatch

object LoginScreen {

  val component = ScalaFnComponent[Unit] { _ =>
    val dispatch                   = useDispatch[ThunkAction[AppActions, AppState, js.Any, AppActions]]()
    val loading                    = useSelector[AppState, AppState](state => state.asInstanceOf[js.Dynamic].uiReducer.loading)
    val (state, handleInputChange) = useForm(Person.default)

    val handleLogin: js.Function1[ReactEventFromInput, Callback] =
      (e: ReactEventFromInput) =>
        Callback {
          dispatch(thunkDispatch(UiAction.StartLoading()))
          (for {
            userCredential <- FirebaseConfiguration.firebase
              .auth()
              .signInWithEmailAndPassword(state.email, state.password)
              .toFuture
          } yield {
            dispatch(
              thunkDispatch(
                AuthAction.Login(
                  Person(
                    userCredential.user.asInstanceOf[User].uid,
                    userCredential.user.asInstanceOf[User].displayName.asInstanceOf[String]
                  )
                )
              )
            )
            dispatch(thunkDispatch(UiAction.FinishLoading()))
          }).recoverWith { case e: Exception =>
            println(e)
            Swal.fire("Error", e.getMessage, SweetAlertIcon.error).toFuture
          }
        } >> e.preventDefaultCB

    val handleGoogleLogin = () =>
      FirebaseConfiguration.firebase.auth().signInWithPopup(FirebaseConfiguration.googleAuthProvider).toFuture.map { userCredential =>
        dispatch(
          thunkDispatch(
            AuthAction.Login(
              Person(
                userCredential.user.asInstanceOf[User].uid,
                userCredential.user.asInstanceOf[User].displayName.asInstanceOf[String]
              )
            )
          )
        )
      }

    Fragment(
      <.h3(^.className := "auth__title")("Login"),
      <.form(^.onSubmit ==> handleLogin)(
        <.input(
          ^.`type`       := text.name,
          ^.placeholder  := "Email",
          ^.name         := "email",
          ^.className    := "auth__input",
          ^.autoComplete := "off",
          ^.value        := state.email,
          ^.onChange ==> handleInputChange
        ),
        <.input(
          ^.`type`       := "password",
          ^.placeholder  := "Password",
          ^.name         := "password",
          ^.className    := "auth__input",
          ^.autoComplete := "off",
          ^.value        := state.password,
          ^.onChange ==> handleInputChange
        ),
        <.button(
          ^.`type`    := "submit",
          ^.className := "btn btn-primary btn-block",
          ^.disabled  := loading.asInstanceOf[Boolean]
        )("Login"),
        <.div(^.className := "auth__social-networks")(
          <.p("Login with social networks"),
          <.div(^.className := "google-btn", ^.onClick --> Callback(handleGoogleLogin()))(
            <.div(^.className   := "google-icon-wrapper")(
              <.img(^.className := "google-icon", ^.src := "https://upload.wikimedia.org/wikipedia/commons/5/53/Google_%22G%22_Logo.svg")
            ),
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
