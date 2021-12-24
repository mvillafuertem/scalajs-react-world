package io.github.mvillafuertem.pokedex

import typings.csstype.csstypeStrings.red
import typings.reactNative.anon.Height
import typings.reactNative.mod.{ Dimensions, FlexAlignType, ImageStyle, TextStyle, ViewStyle }
import typings.reactNative.reactNativeStrings.{ `flex-start`, absolute, bold, center, hidden, row, window }

package object styles {

  val windowWidth = Dimensions.get_window(window).width

  val pokebola: ImageStyle = ImageStyle()
    .setWidth(300)
    .setHeight(300)
    .setPosition(absolute)
    .setTop(-100)
    .setRight(-100)
    .setOpacity(0.2)

  val cardPokebola: ImageStyle = ImageStyle()
    .setWidth(100)
    .setHeight(100)
    .setPosition(absolute)
    .setRight(-25)
    .setBottom(-25)

  val pokemonName: TextStyle = TextStyle()
    .setColor("white")
    .setFontSize(20)
    .setFontWeight(bold)
    .setTop(20)
    .setLeft(10)

  val pokemonImage: ImageStyle = ImageStyle()
    .setWidth(100)
    .setHeight(100)
    .setPosition(absolute)
    .setRight(-10)
    .setBottom(-5)

  val cardContainer: String => ViewStyle = (backgroundColor: String) =>
    ViewStyle()
      .setMarginHorizontal(10)
      .setBackgroundColor(backgroundColor)
      .setHeight(120)
      // .setWidth(160)
      .setWidth(windowWidth * 0.4)
      .setMarginBottom(25)
      .setBorderRadius(10)
      .setShadowColor("#000")
      .setShadowOffset(Height(2, 0))
      .setShadowOpacity(0.25)
      .setShadowRadius(3.84)
      .setElevation(5)

  val pokebolaContainer: ViewStyle = ViewStyle()
    .setWidth(100)
    .setHeight(100)
    .setPosition(absolute)
    .setBottom(0)
    .setRight(0)
    .setOverflow(hidden)
    .setOpacity(0.5)

  val headerContainer: ViewStyle = ViewStyle()
    .setHeight(370)
    .setZIndex(999)
    .setAlignItems(FlexAlignType.center)
    .setBorderBottomRightRadius(1000)
    .setBorderBottomLeftRadius(1000)

  val backButton: ViewStyle = ViewStyle()
    .setPosition(absolute)
    .setLeft(20)

  val pokemonScreenName: TextStyle = TextStyle()
    .setColor("white")
    .setFontSize(40)
    .setAlignSelf(`flex-start`)
    .setLeft(20)

  val pokemonScreenPokebola: ImageStyle = ImageStyle()
    .setWidth(250)
    .setHeight(250)
    .setBottom(-20)
    .setOpacity(0.7)

  val pokemonScreenImage: ImageStyle = ImageStyle()
    .setWidth(250)
    .setHeight(250)
    .setPosition(absolute)
    .setBottom(-15)

  val pokemonScreenLoadingIndicator: ViewStyle = ViewStyle()
    .setFlex(1)
    .setJustifyContent(center)
    .setAlignItems(center)

  val pokemonDetailsContainer: ViewStyle = ViewStyle()
    .setMarginHorizontal(20)

  val pokemonDetailsTitle: TextStyle = TextStyle()
    .setFontSize(20)
    .setFontWeight(bold)

  val pokemonDetailsRegularText: TextStyle = TextStyle()
    .setFontSize(19)
    .setMarginRight(10)

  val pokemonDetailsBasicSprite: ImageStyle = ImageStyle()
    .setWidth(100)
    .setHeight(100)

  val searchInputContainer: ViewStyle = ViewStyle()
  // .setBackgroundColor("red")

  val searchInputTextBackground: ViewStyle = ViewStyle()
    .setBackgroundColor("#F3F1F3")
    .setBorderRadius(50)
    .setHeight(40)
    .setPaddingHorizontal(20)
    .setJustifyContent(center)
    .setAlignItems(center)
    .setFlexDirection(row)
    .setShadowColor("#000")
    .setShadowOffset(Height(2, 0))
    .setShadowOpacity(0.25)
    .setShadowRadius(3.84)
    .setElevation(5)

  val searchInputTextInput: TextStyle = TextStyle()
    .setFlex(1)
    .setFontSize(18)

  val searchInputActivityContainer: ViewStyle =
    ViewStyle()
      .setFlex(1)
      .setBackgroundColor("red")
      .setJustifyContent(center)
      .setAlignItems(center)

}
