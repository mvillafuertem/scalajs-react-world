package io.github.mvillafuertem.pokedex.components

import japgolly.scalajs.react.component.Js
import japgolly.scalajs.react.{ Children, CtorType, JsComponent }
import typings.reactNative.components.Image
import typings.reactNative.components.Image.Builder
import typings.reactNative.mod.{ ImageProps, ImageSourcePropType }

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object AnimatedImage {

  @JSImport("react-native", "Animated.Image")
  @js.native
  object RawComponent extends js.Object

//  def apply(): Image.Builder = {
//    val __props = js.Dynamic.literal()
//    new Image.Builder(js.Array(this.RawComponent, __props.asInstanceOf[ImageProps]))
//  }

  @scala.inline
  def apply(source: ImageSourcePropType): Builder = {
    val __props = js.Dynamic.literal(source = source.asInstanceOf[js.Any])
    new Builder(js.Array(this.RawComponent, __props.asInstanceOf[ImageProps]))
  }

//  val component: Js.Component[ImageProps, Null, CtorType.PropsAndChildren] =
//    JsComponent[ImageProps, Children.Varargs, Null](RawComponent)

}
