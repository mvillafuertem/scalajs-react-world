package io.github.mvillafuertem.pokedex.components

import io.github.mvillafuertem.pokedex.assets.PokebolaPNG
import io.github.mvillafuertem.pokedex.hooks.usePokemonPaginated.SimplePokemon
import io.github.mvillafuertem.pokedex.screen.PokemonScreen.PokemonScreenProps
import io.github.mvillafuertem.pokedex.styles
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ AsyncCallback, Callback, ScalaFnComponent }
import typings.react.mod.{ useEffect, useState, Destructor, EffectCallback }
import typings.reactNative.components.{ Image, Text, TouchableOpacity, View }
import typings.reactNative.mod.ImageSourcePropType
import typings.reactNativeImageColors.mod.{ default => ImageColors }
import typings.reactNativeImageColors.typesMod.Config
import typings.reactNavigationCore.mod.useNavigation

import scala.scalajs.js
import scala.scalajs.js.|

object PokemonCard {

  case class Props(pokemon: SimplePokemon)

  val component = ScalaFnComponent[Props] { case Props(pokemon) =>
    val js.Tuple2(bgColor, setBgColor)     = useState("grey")
    val js.Tuple2(isMounted, setIsMounted) = useState(true)
    val navigation                         = useNavigation[js.Dynamic]()

    useEffect {
      { () =>
        if (!isMounted) { // avoid errors github.com/facebook/react-native/issues/27483
          ()
        } else {
          AsyncCallback
            .fromJsPromise(
              ImageColors
                .getColors(pokemon.picture, Config().setFallback("grey"))
                .asInstanceOf[js.Promise[js.Any]]
                .`then`[Unit](
                  (
                    colors =>
                      if (colors.asInstanceOf[js.Dynamic].platform.asInstanceOf[String] == "android") {
                        setBgColor(colors.asInstanceOf[js.Dynamic].dominant.asInstanceOf[String])
                      } else {
                        setBgColor(colors.asInstanceOf[js.Dynamic].background.asInstanceOf[String])
                      }
                  ): js.Function1[js.Any, Unit | scala.scalajs.js.Thenable[Unit]]
                )
            )
            .runNow()
        }
        (() => setIsMounted(false)): Destructor
      }: EffectCallback
    }

    TouchableOpacity
      .activeOpacity(0.9)
      .onPress(_ =>
        Callback(navigation.navigate("PokemonScreen", new PokemonScreenProps { val simplePokemon: SimplePokemon = pokemon; val color: String = bgColor }))
      )(
        View.style(styles.cardContainer(bgColor))(
          View(
            Text.style(styles.pokemonName)(s"${pokemon.name}\n#${pokemon.id}")
          ),
          View.style(styles.pokebolaContainer)(
            Image(PokebolaPNG.asInstanceOf[ImageSourcePropType])
              .style(styles.cardPokebola)
          ),
          FadeInImage
            .component(
              FadeInImage.Props(
                pokemon.picture,
                styles.pokemonImage
              )
            )
        )
      )
  }

}
