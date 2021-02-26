package io.github.mvillafuertem.theme

import typings.reactNative.mod.{ Dimensions, ScaledSize }
import typings.reactNative.reactNativeStrings

object Sizes {
  // global sizes
  val base     = 8
  val font     = 14
  val radius   = 12
  val padding  = 24
  val padding2 = 36

  // font sizes
  val largeTitle = 50
  val h1         = 30
  val h2         = 22
  val h3         = 16
  val h4         = 14
  val body1      = 30
  val body2      = 20
  val body3      = 16
  val body4      = 14

  private val scaledSize: ScaledSize = Dimensions.get_window(reactNativeStrings.window);

  val width:  Double = scaledSize.width
  val height: Double = scaledSize.height
}
