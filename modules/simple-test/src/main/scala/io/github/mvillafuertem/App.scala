package io.github.mvillafuertem

import slinky.core._
import slinky.core.annotations.react
import slinky.web.html._
import typings.materialUiCore.components.Button
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{HashRouter => Router, Link, Route, Switch}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("resources/App.css", JSImport.Default)
@js.native
object AppCSS extends js.Object

@react object App {

  private val css = AppCSS

  type Props = Unit

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { _ =>
    div(className := "App")(
      h1("Welcome to React (with Scala.js!)"),
      Router(
        div(
          Link[String](to = "/")(
            Button("Home")
          ),
          Link[String](to = "/contacto")(
            Button("Contacto")
          ),
          Link[String](to = "/quien-soy")(
            Button("Quien Soy")
          )
        ),
        Switch(
          Route(
            RouteProps()
              .setPath("/contacto")
              .setRender(_ => Contacto())
          ),
          Route(
            RouteProps()
              .setPath("/quien-soy")
              .setRender(_ => QuienSoy())
          )
        )
      )
    )
  }

}
