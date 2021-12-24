package io.github.mvillafuertem

import io.github.mvillafuertem.InputTextField.InputTextFieldProps
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }
import typings.reactNative.anon.Height
import typings.reactNative.components._
import typings.reactNative.mod.{ ColorValue, ImageRequireSource, ImageResizeMode, ImageSourcePropType, ImageStyle, ImageURISource, TextStyle, ViewStyle }
import typings.reactNative.reactNativeStrings

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object LoginApp {

  type Props = Unit

  @js.native
  @JSImport("../../assets/facebook.png", JSImport.Default)
  object FacebookLogo extends js.Object

  @js.native
  @JSImport("../../assets/google.png", JSImport.Default)
  object GoogleLogo extends js.Object

  val logo = () =>
    Image(GoogleLogo.asInstanceOf[ImageSourcePropType])
      .style(
        ImageStyle()
          .setWidth(150)
          .setHeight(150)
      )
      .resizeMode(ImageResizeMode.cover)

  val styleText = () =>
    TextStyle()
      .setFontFamily("Avenir Next")
      .setColor("#1D2029")

  val styleSocialButton = () =>
    ViewStyle()
      .setFlexDirection(reactNativeStrings.row)
      .setMarginHorizontal(12)
      .setPaddingVertical(12)
      .setPaddingHorizontal(30)
      // TODO
      // .setBorderWidth()
      .setBorderColor("rgba(171, 180, 189, 0.65)")
      .setBorderRadius(4)
      .setBackgroundColor("#FFF")
      .setShadowColor("rgba(171, 180, 189, 0.35)")
      .setShadowOffset(Height(0, 0))
      .setShadowOpacity(1)
      .setShadowRadius(20)
      .setElevation(5)

  val styleSocialLogo = () =>
    ImageStyle()
      .setWidth(16)
      .setHeight(16)
      .setMarginRight(8)

  val styleContainer = () => ViewStyle().setFlex(1).setBackgroundColor("#FFF").setPaddingHorizontal(30)

  val component: Component[Props, CtorType.Nullary] = ScalaFnComponent[Props] { _ =>
    ScrollView.style(styleContainer())(
      View(
        View.style(
          ViewStyle()
            .setMarginTop(60)
            .setAlignItems(reactNativeStrings.center)
            .setJustifyContent(reactNativeStrings.center)
        )(
          logo(),
          Text.style(
            styleText()
              .setMarginTop(10)
              .setFontSize(22)
              .setFontWeight(reactNativeStrings.`500`)
          )(
            "Title Company"
          )
        ),
        View.style(
          ViewStyle()
            .setMarginTop(48)
            .setFlexDirection(reactNativeStrings.row)
            .setJustifyContent(reactNativeStrings.center)
        )(
          TouchableOpacity(
            View.style(styleSocialButton())(
              Image(FacebookLogo.asInstanceOf[ImageSourcePropType]).style(styleSocialLogo()),
              Text("Facebook")
            )
          ),
          TouchableOpacity(
            View.style(styleSocialButton())(
              Image(GoogleLogo.asInstanceOf[ImageSourcePropType]).style(styleSocialLogo()),
              Text("Google")
            )
          )
        ),
        Text.style(
          styleText()
            .setColor("#ABB4BD")
            .setFontSize(15)
            .setTextAlign(reactNativeStrings.center)
            .setMarginVertical(20)
        )("or"),
        InputTextField.component(
          InputTextFieldProps(
            "Email",
            "",
            false,
            ViewStyle()
          )
        ),
        InputTextField.component(
          InputTextFieldProps(
            "Passsword",
            "",
            true,
            ViewStyle()
              .setMarginTop(32)
              .setMarginBottom(8)
          )
        ),
        Text.style(
          styleText()
            .setColor("#FF1654")
            .setFontSize(14)
            .setFontWeight(reactNativeStrings.`500`)
            .setTextAlign(reactNativeStrings.right)
        )("Forget Password"),
        TouchableOpacity.style(
          ViewStyle()
            .setBackgroundColor("#FF1654")
            .setBorderRadius(4)
            .setPaddingVertical(12)
            .setMarginTop(32)
            .setAlignItems(reactNativeStrings.center)
            .setJustifyContent(reactNativeStrings.center)
            .setShadowColor("rgba(255, 22, 84, 0.24)")
            .setShadowOffset(Height(0, 9))
            .setShadowOpacity(1)
            .setShadowRadius(20)
        )(
          View(
            Text.style(styleText().setColor("#FFF").setFontWeight(reactNativeStrings.`600`).setFontSize(16))("Login")
          )
        ),
        Text.style(
          styleText()
            .setFontSize(14)
            .setColor("#ABB4BD")
            .setTextAlign(reactNativeStrings.center)
            .setMarginTop(24)
        )("Don't have an account? ")(
          Text.style(
            styleText()
              .setColor("#FF1654")
              .setFontSize(14)
              .setFontWeight(reactNativeStrings.`500`)
              .setTextAlign(reactNativeStrings.right)
          )("Register Now")
        )
      )
    )
  }

}
