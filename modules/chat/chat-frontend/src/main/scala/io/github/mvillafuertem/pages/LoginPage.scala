package io.github.mvillafuertem.pages

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{ <, _ }
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }
import typings.reactRouterDom.components.Link

object LoginPage {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    <.form(^.className := "login100-form validate-form flex-sb flex-w")(
      <.span(^.className := "login100-form-title mb-3")("Chat - Ingreso"),
      <.div(^.className     := "wrap-input100 validate-input mb-3")(
        <.input(^.className := "input100", ^.`type` := "email", ^.name := "email", ^.placeholder := "Email"),
        <.span(^.className  := "focus-input100")
      ),
      <.div(^.className     := "wrap-input100 validate-input mb-3")(
        <.input(^.className := "input100", ^.`type` := "password", ^.name := "password", ^.placeholder := "Password"),
        <.span(^.className  := "focus-input100")
      ),
      <.div(^.className := "row mb-3")(
        <.div(^.className     := "col")(
          <.input(^.className := "input-checkbox100", ^.id := "ckb1", ^.`type` := "checkbox", ^.name := "remember-me"),
          <.label(^.className := "label-checkbox100")("Recordarme")
        )
      ),
      <.div(^.className := "col text-right")(
        Link[String]("/auth/register").className("txt1")("Nueva cuenta?")
      ),
      <.div(^.className := "container-login100-form-btn m-t-17")(
        <.button(^.className := "login100-form-btn")("Ingresar")
      )
    )
  }

}
