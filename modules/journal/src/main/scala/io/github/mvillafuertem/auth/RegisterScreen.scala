package io.github.mvillafuertem.auth

import io.github.mvillafuertem.hooks.{ useForm, Person }
import japgolly.scalajs.react.React.Fragment
import japgolly.scalajs.react.vdom.SvgTags.text
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ Callback, ReactEventFromInput, ScalaFnComponent }
import typings.reactRouterDom.components.Link
import typings.validator

import scala.scalajs.js

object RegisterScreen {

  val component = ScalaFnComponent[Unit] { _ =>
    val (state, handleInputChange) = useForm(Person.default)

    val isFormValid: Boolean = {

      if (state.name.trim.isEmpty) {
        println("Name is required")
        false
      } else if (validator.isEmailMod.default(state.email)) {
        println("Email is not valid")
        false
      } else if (state.password.length < 6) {
        println("Password should be a least 6 characters")
        false
      } else {
        true
      }
    }

    val handleSubmit: js.Function1[ReactEventFromInput, Callback] =
      (e: ReactEventFromInput) =>
        Callback {
          e.preventDefault()
          if (isFormValid) {
            println(js.JSON.stringify(state))
          }
        }

    Fragment(
      <.h3(^.className := "auth__title")("Register"),
      <.form(^.onSubmit ==> handleSubmit)(
        <.div(^.className := "auth__alert-error")("Hola Mundo"),
        <.input(
          ^.`type` := text.name,
          ^.placeholder := "Name",
          ^.name := "name",
          ^.className := "auth__input",
          ^.autoComplete := "off",
          ^.value := state.name,
          ^.onChange ==> handleInputChange
        ),
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
          ^.className := "btn btn-primary btn-block"
        )("Register"),
        Link[String]("/auth/login").className("link")
      )
    )

  }

}
