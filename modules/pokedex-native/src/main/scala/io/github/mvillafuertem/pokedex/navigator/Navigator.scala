package io.github.mvillafuertem.pokedex.navigator

import io.github.mvillafuertem.pokedex.screen.PokemonScreen.PokemonScreenProps
import io.github.mvillafuertem.pokedex.screen.{HomeScreen, PokemonScreen, SearchScreen}
import japgolly.scalajs.react.raw.React.ComponentClassP
import japgolly.scalajs.react.{Children, JsFnComponent, ScalaFnComponent}
import typings.reactNative.mod.ViewStyle
import typings.reactNavigationCore.typesMod.DefaultNavigatorOptions
import typings.reactNavigationRouters.typesMod.ParamListBase
import typings.reactNavigationStack.typesMod.StackNavigationOptions

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("@react-navigation/stack", JSImport.Namespace)
object RawComponent extends js.Object {
  def createStackNavigator[A](): StackNavigator[A] = js.native
}

@js.native
trait StackNavigator[A] extends js.Object {

  val Navigator: ComponentClassP[js.Object] = js.native
  val Screen:    ComponentClassP[js.Object] = js.native

}

object NavigatorOne {

  // https://github.com/react-navigation/react-navigation/blob/54739828598d7072c1bf7b369659e3682db3edc5/packages/stack/src/navigators/createStackNavigator.tsx#L28
  //val Stack = ReactNavigationStack.createStackNavigator()
  type Props = Unit

  // https://www.typescriptlang.org/docs/handbook/2/objects.html
  trait RootStackParams extends ParamListBase {
    val HomeScreen:    Unit
    val PokemonScreen: PokemonScreenProps
  }

  val navigatorOptions: DefaultNavigatorOptions[StackNavigationOptions, ParamListBase] =
    DefaultNavigatorOptions[StackNavigationOptions, ParamListBase]()
      .setInitialRouteName("HomeScreen")
      .setScreenOptions(
        StackNavigationOptions()
          .setHeaderShown(false)
          .setCardStyle(ViewStyle().setBackgroundColor("white"))
      )

  private val Stack = RawComponent.createStackNavigator()

  val Navigator = JsFnComponent[DefaultNavigatorOptions[StackNavigationOptions, ParamListBase], Children.Varargs](Stack.Navigator)
  val Screen    = JsFnComponent[js.Object, Children.Varargs](Stack.Screen)

  def apply() = ScalaFnComponent[Unit](_ =>
    Navigator(navigatorOptions)(
      Screen(new js.Object {
        val name = "PokemonScreen"
        //val children =
        val component = PokemonScreen.component.toJsComponent.raw
      })(),
      Screen(new js.Object {
        val name = "HomeScreen"
        //val children =
        val component = HomeScreen.component.toJsComponent.raw
      })()
    )
  )

}

object NavigatorTwo {

  def apply() = ScalaFnComponent[Unit](_ =>
    NavigatorOne.Navigator(NavigatorOne.navigatorOptions)(
      NavigatorOne.Screen(new js.Object {
        val name = "PokemonScreen"
        //val children =
        val component = PokemonScreen.component.toJsComponent.raw
      })(),
      NavigatorOne.Screen(new js.Object {
        val name = "HomeScreen"
        //val children =
        val component = SearchScreen.component.toJsComponent.raw
      })()
    )
  )
}
