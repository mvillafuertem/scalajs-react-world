package io.github.mvillafuertem.pages

import io.github.mvillafuertem.auth.AuthContext
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{Callback, CallbackTo, CtorType, ReactEventFromInput, ScalaFnComponent}
import typings.react.mod.{useContext, useState}
import typings.reactRouterDom.components.Link

import scala.scalajs.js

class RegisterFields(val name: String = "", val email: String = "", val password: String = "") extends js.Object

object RegisterPage {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    val chatState = useContext(AuthContext.value)

    val js.Tuple2(form, setForm) = useState(new RegisterFields())

      val handleInputChange: js.Function1[ReactEventFromInput, Callback] =
        (e: ReactEventFromInput) =>
          Callback {
            e.target.name match {
              case "name"     => setForm(new RegisterFields(e.target.value, form.password))
              case "email"    => setForm(new RegisterFields(form.name, e.target.value, form.password))
              case "password" => setForm(new RegisterFields(form.name, form.email, e.target.value))
            }
          }

      val handleSubmit: js.Function1[ReactEventFromInput, Callback] =
        (e: ReactEventFromInput) =>
          e.preventDefaultCB >>
            CallbackTo[Unit] {
              chatState.register.map(_(form.name, form.email, form.password))
            }

      val todoOk: js.Function0[Boolean] = () =>
        if (
          form.name.nonEmpty &&
          form.email.nonEmpty &&
          form.password.nonEmpty
        ) true
        else false

      <.form(^.className := "login100-form validate-form flex-sb flex-w", ^.onSubmit ==> handleSubmit)(
        <.span(^.className := "login100-form-title mb-3")("Chat - Registro"),
        <.div(^.className := "wrap-input100 validate-input mb-3")(
          <.input(
            ^.className   := "input100",
            ^.`type`      := "text",
            ^.name        := "name",
            ^.placeholder := "Nombre",
            ^.value       := form.name,
            ^.onChange ==> handleInputChange
          ),
          <.span(^.className := "focus-input100")
        ),
        <.div(^.className := "wrap-input100 validate-input mb-3")(
          <.input(
            ^.className   := "input100",
            ^.`type`      := "email",
            ^.name        := "email",
            ^.placeholder := "Email",
            ^.value       := form.email,
            ^.onChange ==> handleInputChange
          ),
          <.span(^.className := "focus-input100")
        ),
        <.div(^.className := "wrap-input100 validate-input mb-3")(
          <.input(
            ^.className   := "input100",
            ^.`type`      := "password",
            ^.name        := "password",
            ^.placeholder := "Password",
            ^.value       := form.password,
            ^.onChange ==> handleInputChange
          ),
          <.span(^.className := "focus-input100")
        ),
        <.div(^.className := "row mb-3")(
          <.div(^.className := "col text-right")(
            Link[String]("/auth/login").className("txt1")("Ya tienes cuenta?")
          )
        ),
        <.div(^.className := "container-login100-form-btn m-t-17")(
          <.button(
            ^.`type`    := "submit",
            ^.className := "login100-form-btn",
            ^.disabled  := !todoOk()
          )("Crear cuenta")
        )
      )
    }


}
