package io.github.mvillafuertem
import japgolly.scalajs.react.component.Js
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.{ Callback, CtorType, ScalaFnComponent, _ }
import typings.reactNative.components.{ StatusBar, Text, TouchableOpacity, View }
import typings.reactNative.mod._
import typings.reactNative.reactNativeStrings.{ absolute, window, NamedStyles }

import scala.scalajs.js

object CountdownApp {

  val scaledSize: ScaledSize = Dimensions.^.get(window);
  val black = "#323F4E"
  val red   = "#F76A6A"
  val text  = "#ffffff"

  val timers       = js.Array[Int]().map(i => if (i == 0) 1 else i * 5)
  val ITEM_SIZE    = scaledSize.width * 0.38
  val ITEM_SPACING = (scaledSize.width - ITEM_SIZE) / 2

  val styles: NamedStyles = StyleSheet.create(
    js.Dynamic
      .literal(
        text = js.Dynamic.literal(
          fontSize = ITEM_SIZE * 0.8,
          fontFamily = "Menlo",
          color = text,
          fontWeight = 900
        )
      )
      .asInstanceOf[NamedStyles]
  )

  type Props = Unit

  type State = Int

  val animatedViewComponent: Js.Component[Null, Null, CtorType.Children] =
    JsComponent[Null, Children.Varargs, Null](Animated.View)

  val component: Component[Props, CtorType.Nullary] = ScalaFnComponent[Props] { _ =>
    View.style(
      ViewStyle()
        .setFlex(1)
        .setBackgroundColor(black)
    )(
      StatusBar.hidden(true),
      animatedViewComponent(
        TouchableOpacity
          .onPress(_ => Callback())(
            View.style(
              ViewStyle()
                .setWidth(80)
                .setHeight(80)
                .setBorderRadius(80)
                .setBackgroundColor(red)
            )
          )
      ),
      View.style(
        ViewStyle()
          .setPosition(absolute)
          .setTop(scaledSize.height / 3)
          .setLeft(0)
          .setRight(0)
          .setFlex(1)
      )(
        Text.style(TextStyle().set("text", 1))
      )
    )

  }

}
