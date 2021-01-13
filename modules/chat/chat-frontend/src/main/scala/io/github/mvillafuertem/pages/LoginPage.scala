package io.github.mvillafuertem.pages

import io.github.mvillafuertem.auth.AuthContext
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{Callback, CtorType, ReactEventFromInput, ScalaFnComponent}
import typings.react.mod.{EffectCallback, useEffect, useState}
import typings.reactRouterDom.components.Link
import typings.std.global.{console, localStorage}

import scala.scalajs.js

class Fields(val email: String = "", val password: String = "", val `remember-me`: Boolean = false) extends js.Object

object LoginPage {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
      val js.Tuple2(form, setForm) = useState(new Fields())
      useEffect(
        (() => {
          val email = localStorage.getItem("email")
          if (email.nonEmpty) {
            setForm(new Fields(email, form.password, true))
          }
        }): EffectCallback,
        js.Array[js.Any]()
      )
    AuthContext.value.consume { chatState =>


      val handleInputChange: js.Function1[ReactEventFromInput, Callback] =
        (e: ReactEventFromInput) =>
          Callback {
            e.target.name match {
              case "email"    => setForm(new Fields(e.target.value, form.password, form.`remember-me`))
              case "password" => setForm(new Fields(form.email, e.target.value, form.`remember-me`))
            }
          }

      val toggleCheck = () =>
        Callback {
          setForm(new Fields(form.email, form.password, !form.`remember-me`))
        }

      val handleSubmit: js.Function1[ReactEventFromInput, Callback] =
        (e: ReactEventFromInput) =>
          e.preventDefaultCB >>
            Callback {
              if (form.`remember-me`) {
                localStorage.setItem("email", form.email)
              } else {
                localStorage.removeItem("email")
              }
              console.log("pepep")
              console.log("pepep" + chatState)
              chatState.login.map(_(form.email, form.password)).map(_ =>console.log("popopo") )
              //value.login.map(_(form.email, form.password))
            }

      <.form(
        ^.className := "login100-form validate-form flex-sb flex-w",
        ^.onSubmit ==> handleSubmit
      )(
        <.span(^.className := "login100-form-title mb-3")("Chat - Ingreso"),
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
          <.div(^.className := "col", ^.onClick --> toggleCheck())(
            <.input(
              ^.className := "input-checkbox100",
              ^.id        := "ckb1",
              ^.`type`    := "checkbox",
              ^.name      := "remember-me",
              ^.checked   := form.`remember-me`,
              ^.readOnly  := true
            ),
            <.label(^.className := "label-checkbox100")("Recordarme")
          ),
          <.div(^.className := "col text-right")(
            Link[String]("/auth/register").className("txt1")("Nueva cuenta?")
          )
        ),
        <.div(^.className := "container-login100-form-btn m-t-17")(
          <.button(^.className := "login100-form-btn")("Ingresar")
        )
      )
    }

  }

}
