package io.github.mvillafuertem.screens

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }
import typings.reactNative.components.{ Text, View }

object Home {

  type Props = Unit

  val component: Component[Props, CtorType.Nullary] = ScalaFnComponent[Props] { _ =>
    View(
      Text("Home")
    )

  }

}
