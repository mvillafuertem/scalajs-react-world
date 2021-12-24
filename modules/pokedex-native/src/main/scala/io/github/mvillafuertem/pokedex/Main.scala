package io.github.mvillafuertem.pokedex

import io.github.mvillafuertem.pokedex.navigator.TabNavigator
import japgolly.scalajs.react.ScalaFnComponent
import japgolly.scalajs.react.component.JsFn.UnusedObject
import japgolly.scalajs.react.raw.React.StatelessFunctionalComponent
import typings.reactNavigationNative.components.NavigationContainer

import scala.scalajs.js.annotation.JSExportTopLevel

object Main {

  @JSExportTopLevel("app")
  val app: StatelessFunctionalComponent[UnusedObject] = ScalaFnComponent[Unit](_ => NavigationContainer(TabNavigator())).toJsComponent.raw

}
