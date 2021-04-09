package io.github.mvillafuertem.pokedex.components

import io.github.mvillafuertem.pokedex.hooks.useAnimation
import japgolly.scalajs.react.{Callback, CallbackTo, CtorType, ReactEventFrom, ScalaFnComponent}
import japgolly.scalajs.react.component.ScalaFn.Component
import org.scalajs.dom.raw.Element
import typings.react.mod.useState
import typings.reactNative.components.{ActivityIndicator, Text, View}
import typings.reactNative.mod.{FlexAlignType, ImageErrorEventData, ImageStyle, ImageURISource, NativeSyntheticEvent, NodeHandle, StyleProp, ViewStyle}
import typings.reactNative.reactNativeStrings.{absolute, center}

import scala.scalajs.js

object FadeInImage {

  case class Props(uri: String, style: js.UndefOr[StyleProp[ImageStyle]] = js.undefined)

  val component = ScalaFnComponent[Props] { case Props(uri, style) =>
    val (opacity, _, fadeIn, _, _)         = useAnimation()
    val js.Tuple2(isLoading, setIsLoading) = useState(true)

    val finishLoading = (_: ReactEventFrom[NodeHandle with Element]) =>
      Callback {
        setIsLoading(false)
        fadeIn()
      }
    val onError = (e: NativeSyntheticEvent[ImageErrorEventData]) => Callback(setIsLoading(false))

    View
      .set(
        "style",
        js.Array(
          ViewStyle()
            .setJustifyContent(center)
            .setAlignItems(FlexAlignType.center),
          style
        )
      )(
        ActivityIndicator
          .style(
            ViewStyle()
              .setPosition(absolute)
          )
          .color("grey")
          .size(30)
          .when(isLoading),
        AnimatedImage(ImageURISource().setUri(uri))
          .onError(onError)
          .onLoad(finishLoading)
          .set("style", js.Array(style, ViewStyle().set("opacity", opacity)))
      )
  }

}
