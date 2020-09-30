package io.github.mvillafuertem.components.auth

import io.github.mvillafuertem.actions.{ AppActions, AuthAction, UiAction }
import io.github.mvillafuertem.firebase.FirebaseConfiguration
import io.github.mvillafuertem.hooks.useForm
import io.github.mvillafuertem.model.Person
import io.github.mvillafuertem.reducers.AppState
import japgolly.scalajs.react.React.Fragment
import japgolly.scalajs.react.component.Js
import japgolly.scalajs.react.component.Js.Component
import japgolly.scalajs.react.vdom.SvgTags.text
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ Callback, Children, CtorType, JsComponent, ReactEventFromInput, ScalaFnComponent }
import typings.firebase.anon.DisplayName
import typings.firebase.mod.User
import typings.firebase.mod.auth.UserCredential
import typings.reactRedux.mod.{ connect, useDispatch, useSelector }
import typings.reactRouterDom.components.Link
import typings.reduxThunk.mod.ThunkDispatch
import typings.validator

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.Promise
import scala.util.{ Failure, Success }

object RegisterScreen {

  val component = ScalaFnComponent[Unit] { _ =>
    val dispatch = useDispatch[ThunkDispatch[js.Any, js.Any, AppActions]]()
    val msgError = useSelector[AppState, AppState](state => state.asInstanceOf[js.Dynamic].uiReducer.msgError)

    val (formValues, handleInputChange) = useForm(Person.default)

    val isFormValid: Boolean = {

      if (formValues.name.trim.isEmpty) {
        dispatch(UiAction.SetError("Name is required"))
        false
      } else if (validator.isEmailMod.default(formValues.email)) {
        dispatch(UiAction.SetError("Email is not valid"))
        false
      } else if (formValues.password.length < 6) {
        dispatch(UiAction.SetError("Password should be a least 6 characters"))
        false
      } else {
        true
      }
    }

    val handleSubmit: js.Function1[ReactEventFromInput, Callback] =
      (e: ReactEventFromInput) =>
        Callback(
          if (isFormValid) {
            (for {
              userCredential <- FirebaseConfiguration.firebase
                                  .auth()
                                  .createUserWithEmailAndPassword(formValues.email, formValues.password)
                                  .toFuture
              _              <- userCredential.user
                                  .asInstanceOf[User]
                                  .updateProfile(DisplayName().setDisplayName(formValues.name))
                                  .toFuture
            } yield dispatch(
              AuthAction.Login(
                Person(
                  userCredential.user.asInstanceOf[User].uid,
                  userCredential.user.asInstanceOf[User].displayName.asInstanceOf[String]
                )
              )
            )).recover { case e: Exception => println(e) }
          }
        ) >> e.preventDefaultCB

    Fragment(
      <.h3(^.className := "auth__title")("Register"),
      <.form(^.onSubmit ==> handleSubmit)(
        if (msgError.asInstanceOf[String].isEmpty)
          <.div(^.className := "auth__alert-error")(
            msgError.asInstanceOf[String]
          )
        else <.div(),
        <.input(
          ^.`type` := text.name,
          ^.placeholder := "Name",
          ^.name := "name",
          ^.className := "auth__input",
          ^.autoComplete := "off",
          ^.value := formValues.name,
          ^.onChange ==> handleInputChange
        ),
        <.input(
          ^.`type` := text.name,
          ^.placeholder := "Email",
          ^.name := "email",
          ^.className := "auth__input",
          ^.autoComplete := "off",
          ^.value := formValues.email,
          ^.onChange ==> handleInputChange
        ),
        <.input(
          ^.`type` := "password",
          ^.placeholder := "Password",
          ^.name := "password",
          ^.className := "auth__input",
          ^.autoComplete := "off",
          ^.value := formValues.password,
          ^.onChange ==> handleInputChange
        ),
        <.button(
          ^.`type` := "submit",
          ^.className := "btn btn-primary btn-block mb-5"
        )("Register"),
        Link[String]("/auth/login").className("link mt-5")("Already registered?")
      )
    )

  }

  val mapStateToProps: js.Function1[AppState, js.Dynamic] =
    (state: AppState) => js.Dynamic.literal(state = state.asInstanceOf[js.Dynamic].authReducer)

  val mapDispatchToProps: js.Function1[ThunkDispatch[js.Any, js.Any, AuthAction], js.Dynamic] =
    (dispatch: ThunkDispatch[js.Any, js.Any, AuthAction]) => js.Dynamic.literal(dispatch = dispatch)

  val connectElem: Component[Null, Null, CtorType.Children] =
    JsComponent[Null, Children.Varargs, Null](
      connect.asInstanceOf[js.Dynamic](mapStateToProps, mapDispatchToProps)(this.component.toJsComponent.raw)
    )

}
