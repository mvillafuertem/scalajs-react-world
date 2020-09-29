package io.github.mvillafuertem.components.auth

import io.github.mvillafuertem.actions.{ AuthAction, UiAction }
import io.github.mvillafuertem.firebase.FirebaseConfiguration
import io.github.mvillafuertem.hooks.useForm
import io.github.mvillafuertem.model.Person
import io.github.mvillafuertem.reducers.AppState
import japgolly.scalajs.react.React.Fragment
import japgolly.scalajs.react.component.Js
import japgolly.scalajs.react.vdom.SvgTags.text
import japgolly.scalajs.react.vdom.html_<^.{ <, _ }
import japgolly.scalajs.react.{ Callback, Children, CtorType, JsComponent, ReactEventFromInput, ScalaFnComponent }
import typings.firebase.mod.User
import typings.reactRedux.mod.{ connect, useSelector }
import typings.reduxThunk.mod.ThunkDispatch

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

object LoginScreen {

  @js.native
  trait Props extends js.Object {
    val state: AppState
    val dispatch: ThunkDispatch[js.Any, js.Any, AuthAction]
  }

  val component = ScalaFnComponent[Props] { props =>
    val loading                    = useSelector[AppState, AppState](state => state.asInstanceOf[js.Dynamic].uiReducer.loading)
    val (state, handleInputChange) = useForm(props.state.asInstanceOf[js.Dynamic].person.asInstanceOf[Person])

    val handleLogin: js.Function1[ReactEventFromInput, Callback] =
      (e: ReactEventFromInput) =>
        Callback {
          props.dispatch(UiAction.StartLoading())
          (for {
            userCredential <- FirebaseConfiguration.firebase
                                .auth()
                                .signInWithEmailAndPassword(state.email, state.password)
                                .toFuture
          } yield {
            props.dispatch(
              AuthAction.Login(
                Person(
                  userCredential.user.asInstanceOf[User].uid,
                  userCredential.user.asInstanceOf[User].displayName.asInstanceOf[String]
                )
              )
            )
            props.dispatch(UiAction.FinishLoading())

          }).recover { case e: Exception => println(e) }
        } >> e.preventDefaultCB

    val handleGoogleLogin = () =>
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
        )("Login")
      ),
      <.div(^.className := "auth__social-networks")(
        <.p("Login with social networks"),
        <.div(^.className := "google-btn", ^.onClick --> Callback(handleGoogleLogin()))(
          <.div(^.className := "google-icon-wrapper")(
            <.img(^.className := "google-icon", ^.src := "")),
          <.p(^.className := "btn-text")(
            <.b("Sign in with google")
          )
        )
      )
    )

  }

  val mapStateToProps: js.Function1[AppState, js.Dynamic] =
    (state: AppState) => js.Dynamic.literal(state = state.asInstanceOf[js.Dynamic].authReducer)

  val mapDispatchToProps: js.Function1[ThunkDispatch[js.Any, js.Any, AuthAction], js.Dynamic] =
    (dispatch: ThunkDispatch[js.Any, js.Any, AuthAction]) => js.Dynamic.literal(dispatch = dispatch)

  val connectElem: Js.Component[LoginScreen.Props, Null, CtorType.PropsAndChildren] =
    JsComponent[LoginScreen.Props, Children.Varargs, Null](
      connect.asInstanceOf[js.Dynamic](mapStateToProps, mapDispatchToProps)(this.component.toJsComponent.raw)
    )

}
