package io.github.mvillafuertem.pokedex.navigator

import io.github.mvillafuertem.pokedex.screen.{ HomeScreen, SearchScreen }
import japgolly.scalajs.react.component.Generic
import japgolly.scalajs.react.component.Generic.ComponentRaw
import japgolly.scalajs.react.{ Children, JsFnComponent }
import typings.reactNative.mod._
import typings.reactNative.reactNativeStrings.absolute
import typings.reactNativeVectorIcons.components.{ Ionicons => Icon }
import typings.reactNavigationBottomTabs.createBottomTabNavigatorMod
import typings.reactNavigationBottomTabs.mod.createBottomTabNavigator
import typings.reactNavigationBottomTabs.typesMod.{ BottomTabBarOptions, BottomTabNavigationConfig, BottomTabNavigationOptions }
import typings.reactNavigationCore.typesMod.DefaultNavigatorOptions
import typings.reactNavigationRouters.tabRouterMod.TabRouterOptions
import typings.reactNavigationRouters.typesMod.ParamListBase

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("@react-navigation/bottom-tabs", JSImport.Namespace)
object RawComponentTabs extends js.Object {
  def createBottomTabNavigator[A](): StackNavigator[A] = js.native
}

object TabNavigator {

  private val Tab = RawComponentTabs.createBottomTabNavigator()

  private val Navigator = JsFnComponent[createBottomTabNavigatorMod.Props, Children.Varargs](Tab.Navigator)
  private val Screen    = JsFnComponent[js.Object, Children.Varargs](Tab.Screen)

  def apply() =
    Navigator(
      BottomTabNavigationConfig[BottomTabBarOptions]
        .setTabBarOptions(
          BottomTabBarOptions()
            .setActiveTintColor("#5856D6")
            .setStyle(
              ViewStyle()
                .setBorderWidth(0)
                .setElevation(0)
                .setPosition(absolute)
                .setBackgroundColor("rgba(255, 255, 255, 0.92)")
            )
            .setLabelStyle(
              TextStyle()
                .setMarginBottom((if (Platform.asInstanceOf[PlatformIOSStatic].OS == PlatformOSType.ios) 0 else 10))
            )
        )
        .combineWith(DefaultNavigatorOptions[BottomTabNavigationOptions, ParamListBase])
        .asInstanceOf[createBottomTabNavigatorMod.Props]
    )(
      Screen(new js.Object {
        val name      = "HomeScreen"
        val component = NavigatorOne().toJsComponent.raw
        val options = BottomTabNavigationOptions()
          .setTabBarLabel("Listado")
          .setTabBarIcon(focused => Icon("list-outline").color(focused.color).size(25).rawElement)
      })(),
      Screen(new js.Object {
        val name      = "SearchScreen"
        val component = NavigatorTwo().toJsComponent.raw
        val options = BottomTabNavigationOptions()
          .setTabBarLabel("Listado")
          .setTabBarIcon(focused => Icon("search-outline").color(focused.color).size(25).rawElement)
      })()
    )

}
