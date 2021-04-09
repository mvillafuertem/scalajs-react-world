package io.github.mvillafuertem.pokedex.components

import io.github.mvillafuertem.pokedex.hooks.useDebouncedValue
import io.github.mvillafuertem.pokedex.styles
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.{Callback, CtorType, ScalaFnComponent}
import typings.react.mod.{Dispatch, EffectCallback, SetStateAction, useEffect, useState}
import typings.reactNative.components.{TextInput, View}
import typings.reactNative.mod.global.console
import typings.reactNative.mod.{StyleProp, ViewStyle}
import typings.reactNative.reactNativeStrings.none
import typings.reactNativeVectorIcons.components.{Ionicons => Icon}

import scala.scalajs.js

object SearchInput {

  case class Props(onDebounce: Dispatch[SetStateAction[String]], style: js.UndefOr[StyleProp[ViewStyle]])

  val component: Component[Props, CtorType.Props] = ScalaFnComponent[Props] { case Props(onDebounce, style) =>
    val js.Tuple2(textValue, setTextValue) = useState("")
    val debouncedValue                     = useDebouncedValue(textValue)

    useEffect({ () => onDebounce(debouncedValue)}: EffectCallback, js.Array(debouncedValue))

    View
      .set(
        "style",
        js.Array(
          styles.searchInputContainer,
          style
        )
      )(
        View
          .set(
            "style",
            js.Array(
              styles.searchInputTextBackground
            )
          )(
            TextInput
              .set(
                "style",
                js.Array(
                  styles.searchInputTextInput
                )
              )
              .onChangeText(value => Callback(setTextValue(value)))
              .placeholder("Introduce nombre de pokemon")
              .autoCapitalize(none)
              .autoCorrect(false),
            Icon("search-outline")
              .color("grey")
              .size(30)
          )
      )

  }

}
