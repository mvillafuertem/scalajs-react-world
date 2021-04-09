package io.github.mvillafuertem.pokedex.screen

import io.github.mvillafuertem.pokedex.assets.PokebolaBlancaPNG
import io.github.mvillafuertem.pokedex.components.{ FadeInImage, PokemonDetails }
import io.github.mvillafuertem.pokedex.hooks.usePokemon
import io.github.mvillafuertem.pokedex.hooks.usePokemonPaginated.SimplePokemon
import io.github.mvillafuertem.pokedex.navigator.NavigatorOne.RootStackParams
import io.github.mvillafuertem.pokedex.styles
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ Callback, CtorType, ScalaFnComponent }
import typings.reactNative.components._
import typings.reactNative.mod.{ ImageSourcePropType, ViewStyle }
import typings.reactNativeSafeAreaContext.mod.useSafeAreaInsets
import typings.reactNativeVectorIcons.components.{ Ionicons => Icon }
import typings.reactNavigationStack.typesMod.StackScreenProps

import scala.scalajs.js

object PokemonScreen {

  trait PokemonScreenProps extends js.Object {
    val simplePokemon: SimplePokemon
    val color:         String
  }

  @js.native
  trait Props extends StackScreenProps[RootStackParams, "PokemonScreen"] {}

  val component: Component[Props, CtorType.Props] = ScalaFnComponent[Props] { props: Props =>
    val dynamic              = props.asInstanceOf[js.Dynamic]
    val pokemonScreenProps   = dynamic.route.params.asInstanceOf[PokemonScreenProps]
    val top                  = useSafeAreaInsets().top
    val (isLoading, pokemon) = usePokemon(pokemonScreenProps.simplePokemon.id)

    View.style(ViewStyle().setFlex(1))(
      View
        .set(
          "style",
          js.Array(
            styles.headerContainer,
            ViewStyle()
              .setBackgroundColor(pokemonScreenProps.color)
          )
        )(
          TouchableOpacity
            .onPress(_ => Callback(dynamic.navigation.pop()))
            .set(
              "style",
              js.Array(
                styles.backButton,
                ViewStyle().setTop(top + 5)
              )
            )
            .activeOpacity(0.8)(
              Icon("arrow-back-outline")
                .color("white")
                .size(35)
            ),
          Text
            .set(
              "style",
              js.Array(
                styles.pokemonScreenName,
                ViewStyle().setTop(top + 40)
              )
            )(
              s"${pokemonScreenProps.simplePokemon.name}\n#${pokemonScreenProps.simplePokemon.id}"
            ),
          Image(PokebolaBlancaPNG.asInstanceOf[ImageSourcePropType])
            .style(styles.pokemonScreenPokebola),
          FadeInImage.component(
            FadeInImage.Props(pokemonScreenProps.simplePokemon.picture, styles.pokemonScreenImage)
          )
        ),
      View
        .style(styles.pokemonScreenLoadingIndicator)(
          ActivityIndicator.color(pokemonScreenProps.color).size(50)
        )
        .when(isLoading),
      PokemonDetails
        .component(PokemonDetails.Props(pokemon))
        .when(!isLoading)
    )
  }

}
