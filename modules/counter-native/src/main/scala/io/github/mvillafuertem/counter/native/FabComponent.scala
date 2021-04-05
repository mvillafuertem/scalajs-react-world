package io.github.mvillafuertem.counter.native

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ CallbackTo, CtorType, ScalaFnComponent }
import typings.reactNative.anon.Height
import typings.reactNative.components.{ Text, TouchableNativeFeedback, TouchableOpacity, View }
import typings.reactNative.mod._
import typings.reactNative.reactNativeStrings.{ absolute, bold, center }

import scala.scalajs.js
import scala.scalajs.js.|

object FabComponent {

  sealed trait FabStyleKey
  final case object Fab extends FabStyleKey
  final case object FabLocationBottom extends FabStyleKey
  final case object FabLocationBottomLeft extends FabStyleKey
  final case object FabLocationBottomRight extends FabStyleKey
  final case object FabText extends FabStyleKey

  val styles: js.Map[FabStyleKey, ViewStyle | TextStyle] = StyleSheet.create(
    js.Map[FabStyleKey, ViewStyle | TextStyle](
      Fab -> ViewStyle()
        .setBackgroundColor("#5856D6")
        .setWidth(60)
        .setHeight(60)
        .setBorderRadius(100)
        .setJustifyContent(center)
        .setShadowColor("#000")
        .setShadowOffset(Height(4, 0))
        .setShadowOpacity(0.30)
        .setShadowRadius(4.65)
        .setElevation(8),
      FabLocationBottom -> ViewStyle()
        .setPosition(absolute)
        .setBottom(25),
      FabLocationBottomRight -> ViewStyle()
        .setRight(25),
      FabLocationBottomLeft -> ViewStyle()
        .setLeft(25),
      FabText -> TextStyle()
        .setColor("white")
        .setFontSize(25)
        .setFontWeight(bold)
        .setAlignSelf(center)
    )
  )

  sealed trait FabPosition
  final object BottomLeft extends FabPosition
  final object BottomRight extends FabPosition

  case class Props(
    title:    String,
    onPress:  js.Function0[Unit],
    position: js.UndefOr[FabPosition] = BottomRight
  )

  val component: Component[Props, CtorType.Props] = ScalaFnComponent[Props] { case Props(title, onPress, position) =>
    val ios = () => {
      TouchableOpacity
        .activeOpacity(0.75)
        .set(
          "style",
          js.Array(
            styles(FabLocationBottom),
            position.fold(styles(FabLocationBottomRight)) {
              case BottomLeft  => styles(FabLocationBottomLeft)
              case BottomRight => styles(FabLocationBottomRight)
            }
          )
        )
        .onPress(_ => CallbackTo(onPress()))(
          View.style(styles(Fab).toViewStyle)(
            Text.style(styles(FabText).toTextStyle)(title)
          )
        )
    }

    val android = () => {
      View.set(
        "style",
        js.Array(
          styles(FabLocationBottom),
          position.fold(styles(FabLocationBottomRight)) {
            case BottomLeft  => styles(FabLocationBottomLeft)
            case BottomRight => styles(FabLocationBottomRight)
          }
        )
      )(
        TouchableNativeFeedback
          .background(RippleBackgroundPropType(false).set("color", "#28425B").setRippleRadius(30))
          .onPress(_ => CallbackTo(onPress()))(
            View.style(styles(Fab).toViewStyle)(
              Text.style(styles(FabText).toTextStyle)(title)
            )
          )
      )
    }

    if (Platform.asInstanceOf[PlatformIOSStatic].OS == PlatformOSType.ios)
      ios()
    else if (Platform.asInstanceOf[PlatformAndroidStatic].OS == PlatformOSType.android)
      android()
    else
      Text("an implementation is missing")

  }

}
