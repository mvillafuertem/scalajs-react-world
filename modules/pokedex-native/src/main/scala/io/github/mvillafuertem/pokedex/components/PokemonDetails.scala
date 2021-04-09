package io.github.mvillafuertem.pokedex.components

import io.github.mvillafuertem.pokedex.hooks.usePokemonPaginated.PokemonFull
import io.github.mvillafuertem.pokedex.styles
import japgolly.scalajs.react.ScalaFnComponent
import japgolly.scalajs.react.vdom.html_<^._
import typings.reactNative.components.{ ScrollView, Text, View }
import typings.reactNative.mod.{ FlexAlignType, StyleSheet, TextStyle, ViewStyle }
import typings.reactNative.reactNativeStrings.{ bold, row, wrap }

import scala.scalajs.js

object PokemonDetails {

  case class Props(pokemon: PokemonFull)

  val component = ScalaFnComponent[Props] { case Props(pokemon) =>
    ScrollView
      .showsVerticalScrollIndicator(false)
      .set("style", js.Array(StyleSheet.absoluteFillObject))(
        View.set("style", js.Array(styles.pokemonDetailsContainer, ViewStyle().setMarginTop(370)))(
          Text.style(styles.pokemonDetailsTitle)("Types"),
          View.style(ViewStyle().setFlexDirection(row))(
            pokemon.types
              .map(`type` =>
                Text
                  .style(styles.pokemonDetailsRegularText)
                  .withKey(`type`.`type`.name)(`type`.`type`.name)
              )
              .toVdomArray
          )
        ),
        View.set("style", js.Array(styles.pokemonDetailsContainer, ViewStyle().setMarginTop(20)))(
          Text.style(styles.pokemonDetailsTitle)("Peso"),
          Text.style(styles.pokemonDetailsRegularText)(s"${pokemon.weight}Kg")
        ),
        View.set("style", js.Array(styles.pokemonDetailsContainer, ViewStyle().setMarginTop(20)))(
          Text.style(styles.pokemonDetailsTitle)("Sprites")
        ),
        ScrollView
          .horizontal(true)
          .showsHorizontalScrollIndicator(false)(
            FadeInImage.component(FadeInImage.Props(pokemon.sprites.front_default, styles.pokemonDetailsBasicSprite)),
            FadeInImage.component(FadeInImage.Props(pokemon.sprites.back_default, styles.pokemonDetailsBasicSprite)),
            FadeInImage.component(FadeInImage.Props(pokemon.sprites.front_shiny, styles.pokemonDetailsBasicSprite)),
            FadeInImage.component(FadeInImage.Props(pokemon.sprites.back_shiny, styles.pokemonDetailsBasicSprite))
          ),
        View.set("style", js.Array(styles.pokemonDetailsContainer, ViewStyle().setMarginTop(20)))(
          Text.style(styles.pokemonDetailsTitle)("Habilidades base"),
          View.style(ViewStyle().setFlexDirection(row))(
            pokemon.abilities
              .map(ability =>
                Text
                  .set("style", js.Array(styles.pokemonDetailsRegularText, TextStyle().setMarginRight(10)))
                  .withKey(ability.ability.name)(ability.ability.name)
              )
              .toVdomArray
          )
        ),
        View.set("style", js.Array(styles.pokemonDetailsContainer, ViewStyle().setMarginTop(20)))(
          Text.style(styles.pokemonDetailsTitle)("Movimientos"),
          View.style(ViewStyle().setFlexDirection(row).setFlexWrap(wrap))(
            pokemon.moves
              .map(move =>
                Text
                  .set("style", js.Array(styles.pokemonDetailsRegularText, TextStyle().setMarginRight(10)))
                  .withKey(move.move.name)(move.move.name)
              )
              .toVdomArray
          )
        ),
        View.set("style", js.Array(styles.pokemonDetailsContainer, ViewStyle().setMarginTop(20)))(
          Text.style(styles.pokemonDetailsTitle)("Stats"),
          View(
            pokemon.stats.zipWithIndex.map { case (stat, index) =>
              View
                .style(ViewStyle().setFlexDirection(row))
                .withKey(s"${stat.base_stat} + $index")(
                  Text
                    .set("style", js.Array(styles.pokemonDetailsRegularText, TextStyle().setMarginRight(10).setWidth(150)))(stat.stat.name),
                  Text
                    .set("style", js.Array(styles.pokemonDetailsRegularText, TextStyle().setFontWeight(bold)))(stat.base_stat)
                )
            }.toVdomArray
          )
        ),
        View.set("style", js.Array(ViewStyle().setMarginTop(20).setAlignItems(FlexAlignType.center)))(
        )(
          FadeInImage.component(FadeInImage.Props(pokemon.sprites.front_default, styles.pokemonDetailsBasicSprite))
        )
      )
  }

}
