package io.github.mvillafuertem.pokedex.screen

import io.github.mvillafuertem.pokedex.assets.PokebolaPNG
import io.github.mvillafuertem.pokedex.components.PokemonCard
import io.github.mvillafuertem.pokedex.hooks.usePokemonPaginated
import io.github.mvillafuertem.pokedex.styles
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.{CtorType, ScalaFnComponent}
import typings.react.components.Fragment
import typings.reactNative.components.{ActivityIndicator, FlatList, Image, Text, View}
import typings.reactNative.mod.{ImageSourcePropType, ImageStyle, TextStyle, ViewStyle}
import typings.reactNative.reactNativeStrings.{absolute, bold, center}
import typings.reactNativeSafeAreaContext.mod.useSafeAreaInsets
import typings.reactNativeSafeAreaContext.safeAreaTypesMod
import japgolly.scalajs.react.vdom.html_<^._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object HomeScreen {

  type Props = Unit

  val component: Component[Props, CtorType.Nullary] = ScalaFnComponent[Props] { _ =>
    val edgeInsets: safeAreaTypesMod.EdgeInsets = useSafeAreaInsets()
    val (isLoading, simplePokemonList, loadPokemons) = usePokemonPaginated()

    Fragment(
      Image(PokebolaPNG.asInstanceOf[ImageSourcePropType])
        .style(styles.pokebola),
      View.style(ViewStyle().setAlignItems(center))(
      FlatList()
        .data(simplePokemonList)
        .keyExtractor((pokemon, _) => pokemon.id)
        .showsVerticalScrollIndicator(false)
        .numColumns(2)
        .renderItem(value => PokemonCard.component(PokemonCard.Props(value.item)).raw)
        .onEndReached(_ => loadPokemons().toCallback)
        .onEndReachedThreshold(0.4)
        .ListHeaderComponent(
                Text
                  .style(
                    TextStyle()
                      .setFontSize(35)
                      .setFontWeight(bold)
                      .setTop(edgeInsets.top + 20)
                      .setMarginBottom(edgeInsets.top + 20)
                      .setPaddingBottom(10)
                      .setMarginHorizontal(20) // global margin
                  )("HomeScreen")
                  .rawElement
        )
        .ListFooterComponent(
          ActivityIndicator()
            .style(
              ViewStyle()
                .setHeight(100)
            )
            .size(20)
            .color("gray")
            .rawElement
        )
      )

    )
  }

}
