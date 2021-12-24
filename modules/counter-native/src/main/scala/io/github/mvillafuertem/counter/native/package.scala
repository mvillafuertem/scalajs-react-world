package io.github.mvillafuertem.counter

import typings.reactNative.mod.{ TextStyle, ViewStyle }

import scala.scalajs.js.|

package object native {
  implicit class AsInstanceOf(x: ViewStyle | TextStyle) {
    final def toTextStyle: TextStyle = x.asInstanceOf[TextStyle]
    final def toViewStyle: ViewStyle = x.asInstanceOf[ViewStyle]
  }
}
