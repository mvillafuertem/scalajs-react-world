package io.github.mvillafuertem

import japgolly.scalajs.react.component.JsFn.UnusedObject
import japgolly.scalajs.react.raw.React.StatelessFunctionalComponent

import scala.scalajs.js.annotation.JSExportTopLevel

object Main {

  @JSExportTopLevel("app")
  val app: StatelessFunctionalComponent[UnusedObject] = LoginApp.component.toJsComponent.raw

}
