package io.github.mvillafuertem.pages

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{CtorType, ScalaFnComponent}
import typings.reactRouterDom.components.Link

object RegisterPage {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    <.form(^.className := "login100-form validate-form flex-sb flex-w")(
      <.span(^.className := "login100-form-title mb-3")("Chat - Registro"),
      <.div(^.className     := "wrap-input100 validate-input mb-3")(
        <.input(^.className := "input100", ^.`type` := "text", ^.name := "name", ^.placeholder := "Nombre"),
        <.span(^.className  := "focus-input100")
      ),
      <.div(^.className     := "wrap-input100 validate-input mb-3")(
        <.input(^.className := "input100", ^.`type` := "email", ^.name := "email", ^.placeholder := "Email"),
        <.span(^.className  := "focus-input100")
      ),
      <.div(^.className     := "wrap-input100 validate-input mb-3")(
        <.input(^.className := "input100", ^.`type` := "password", ^.name := "password", ^.placeholder := "Password"),
        <.span(^.className  := "focus-input100")
      ),
      <.div(^.className := "row mb-3")(
        <.div(^.className := "col text-right")(
          Link[String]("/auth/login").className("txt1")("Ya tienes cuenta?")
        )
      ),
      <.div(^.className := "container-login100-form-btn m-t-17")(
        <.button(^.className := "login100-form-btn")("Crear cuenta")
      )
    )
  }

}
