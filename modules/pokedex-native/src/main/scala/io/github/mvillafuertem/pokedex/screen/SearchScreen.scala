package io.github.mvillafuertem.pokedex.screen

import io.github.mvillafuertem.pokedex.components.{ PokemonCard, SearchInput }
import io.github.mvillafuertem.pokedex.hooks.usePokemonPaginated.SimplePokemon
import io.github.mvillafuertem.pokedex.hooks.usePokemonSearch
import io.github.mvillafuertem.pokedex.styles
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }
import typings.react.mod.{ useEffect, useState, Dispatch, EffectCallback, SetStateAction }
import typings.reactNative.components.{ ActivityIndicator, FlatList, Text, View }
import typings.reactNative.mod._
import typings.reactNative.reactNativeStrings.{ absolute, bold }
import typings.reactNativeSafeAreaContext.mod.useSafeAreaInsets

import scala.scalajs.js
import scala.util.{ Failure, Success, Try }

object SearchScreen {

  type Props = Unit
  val component: Component[Props, CtorType.Nullary] = ScalaFnComponent[Props] { _ =>
    val top                                            = useSafeAreaInsets().top
    val (isFetching, simplePokemonList)                = usePokemonSearch()
    val js.Tuple2(filteredPokemon, setFilteredPokemon) = useState(js.Array[SimplePokemon]())

    val js.Tuple2(term, setTerm: Dispatch[SetStateAction[String]]) = useState("")

    useEffect(
      { () =>
        Try(term.toInt) match {
          case Failure(_) =>
            if (term.isEmpty) {
              setFilteredPokemon(js.Array[SimplePokemon]())
            } else {
              setFilteredPokemon(simplePokemonList.filter(_.name.toLowerCase.contains(term.toLowerCase())))
            }
          case Success(value) =>
            setFilteredPokemon(simplePokemonList.filter(_.id.toInt == value))
        }

      }: EffectCallback,
      js.Array(term)
    )

    if (isFetching) {
      View.style(styles.searchInputActivityContainer)(
        ActivityIndicator.size(50).color("grey"),
        Text("Cargando...")
      )
    } else {
      View
        .style(
          ViewStyle()
            .setFlex(1)
            // .setMarginTop(if (Platform.asInstanceOf[PlatformIOSStatic].OS == PlatformOSType.ios) top else top + 10)
            .setMarginHorizontal(20)
        )(
          SearchInput.component(
            SearchInput.Props(
              setTerm,
              ViewStyle()
                .setPosition(absolute)
                .setZIndex(999)
                .setWidth(styles.windowWidth)
                .setTop(if (Platform.asInstanceOf[PlatformIOSStatic].OS == PlatformOSType.ios) top else top + 10)
            )
          ),
          FlatList()
            .data(filteredPokemon)
            .keyExtractor((pokemon, _) => pokemon.id)
            .showsVerticalScrollIndicator(false)
            .numColumns(2)
            .ListHeaderComponent(
              Text
                .style(
                  TextStyle()
                    .setFontSize(35)
                    .setFontWeight(bold)
                    .setTop(top + 20)
                    .setPaddingBottom(10)
                    .setMarginTop(if (Platform.asInstanceOf[PlatformIOSStatic].OS == PlatformOSType.ios) top + 60 else top + 80)
                    .setMarginHorizontal(20) // global margin
                )(term)
                .rawElement
            )
            .renderItem(value => PokemonCard.component(PokemonCard.Props(value.item)).raw)
        )
    }

  }

}
