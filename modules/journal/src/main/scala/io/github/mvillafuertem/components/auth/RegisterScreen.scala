package io.github.mvillafuertem.components.auth

import io.github.mvillafuertem.actions.{ AppActions, AuthAction, UiAction }
import io.github.mvillafuertem.firebase.FirebaseConfiguration
import io.github.mvillafuertem.hooks.useForm
import io.github.mvillafuertem.model.Person
import io.github.mvillafuertem.reducers.AppState
import io.github.mvillafuertem.store.thunkDispatch
import japgolly.scalajs.react.React.Fragment
import japgolly.scalajs.react.vdom.SvgTags.text
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ Callback, ReactEventFromInput, ScalaFnComponent }
import typings.firebase.anon.DisplayName
import typings.firebase.mod.User
import typings.reactRedux.mod.{ useDispatch, useSelector }
import typings.reactRouterDom.components.Link
import typings.reduxThunk.mod.ThunkAction
import typings.sweetalert2.mod.{ SweetAlertIcon, default => Swal }
import typings.validator

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

object RegisterScreen {

  val component = ScalaFnComponent[Unit] { _ =>
    val dispatch = useDispatch[ThunkAction[AppActions, AppState, js.Any, AppActions]]()
    val msgError = useSelector[AppState, AppState](state => state.asInstanceOf[js.Dynamic].uiReducer.msgError)

    val (formValues, handleInputChange) = useForm(Person.default)

    val isFormValid: js.Function0[Boolean] = () => {
      if (formValues.name.trim.isEmpty) {
        dispatch(thunkDispatch(UiAction.SetError("Name is required")))
        //dispatch(UiAction.SetError("Name is required"))
        false
      } else if (validator.isEmailMod.default(formValues.email)) {
        dispatch(thunkDispatch(UiAction.SetError("Email is not valid")))
        //dispatch(UiAction.SetError("Email is not valid"))
        false
      } else if (formValues.password.length < 6) {
        dispatch(thunkDispatch(UiAction.SetError("Password should be a least 6 characters")))
        //dispatch(UiAction.SetError("Password should be a least 6 characters"))
        false
      } else {
        true
      }
    }

    val handleSubmit: js.Function1[ReactEventFromInput, Callback] =
      (e: ReactEventFromInput) =>
        Callback(
          if (isFormValid()) {
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
              thunkDispatch(
                AuthAction.Login(
                  Person(
                    userCredential.user.asInstanceOf[User].uid,
                    userCredential.user.asInstanceOf[User].displayName.asInstanceOf[String]
                  )
                )
              )
            )).recover { case e: Exception =>
              println(e)
              Swal.fire("Error", e.getMessage, SweetAlertIcon.error).toFuture
            }
          }
        ) >> e.preventDefaultCB

    Fragment(
      <.h3(^.className := "auth__title")("Register"),
      <.form(^.onSubmit ==> handleSubmit)(
        if (msgError.asInstanceOf[String].nonEmpty)
          <.div(^.className := "auth__alert-error")(
            msgError.asInstanceOf[String]
          )
        else <.div(),
        <.input(
          ^.`type`       := text.name,
          ^.placeholder  := "Name",
          ^.name         := "name",
          ^.className    := "auth__input",
          ^.autoComplete := "off",
          ^.value        := formValues.name,
          ^.onChange ==> handleInputChange
        ),
        <.input(
          ^.`type`       := text.name,
          ^.placeholder  := "Email",
          ^.name         := "email",
          ^.className    := "auth__input",
          ^.autoComplete := "off",
          ^.value        := formValues.email,
          ^.onChange ==> handleInputChange
        ),
        <.input(
          ^.`type`       := "password",
          ^.placeholder  := "Password",
          ^.name         := "password",
          ^.className    := "auth__input",
          ^.autoComplete := "off",
          ^.value        := formValues.password,
          ^.onChange ==> handleInputChange
        ),
        <.button(
          ^.`type`    := "submit",
          ^.className := "btn btn-primary btn-block mb-5"
        )("Register"),
        Link[String]("/auth/login").className("link mt-5")("Already registered?")
      )
    )

  }

}
