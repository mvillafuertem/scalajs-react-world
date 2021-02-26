package io.github.mvillafuertem

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }
import typings.reactNative.components._
import typings.reactNative.mod.{ TextStyle, ViewStyle }

object InputTextField {

  case class InputTextFieldProps(title: String, placeholder: String, isSecure: Boolean, style: ViewStyle)

  val styleInputTitle = () =>
    TextStyle()
      .setColor("#ABB4BD")
      .setFontSize(14)

  val styleInput = () =>
    TextStyle()
      .setPaddingVertical(12)
      .setColor("#1D2029")
      .setFontSize(14)
      .setFontFamily("Avenir Next")

  val component: Component[InputTextFieldProps, CtorType.Props] = ScalaFnComponent[InputTextFieldProps] { props =>
    View.style(props.style)(
      Text.style(
        styleInputTitle()
      )(props.title),
      TextInput()
        .placeholder(props.placeholder)
        .secureTextEntry(props.isSecure)
        .style(styleInput()),
      View.style(ViewStyle().setBorderBottomWidth(1).setBorderBottomColor("#D8D8D8"))
    )

  }

}
