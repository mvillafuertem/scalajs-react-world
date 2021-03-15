package io.github.mvillafuertem

import io.github.mvillafuertem.screens.Home
import io.github.mvillafuertem.screens.Home.Props
import japgolly.scalajs.react.component.JsFn.UnusedObject
import japgolly.scalajs.react.raw.React.StatelessFunctionalComponent
import japgolly.scalajs.react.{ Children, JsComponent, ScalaFnComponent }
import typings.reactNavigationCore.typesMod.DefaultNavigatorOptions
import typings.reactNavigationNative.components.NavigationContainer
import typings.reactNavigationNative.mod.DefaultTheme
import typings.reactNavigationStack.mod.createStackNavigator
import typings.std.Record

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object App {

  val theme = DefaultTheme.setColors(DefaultTheme.colors.setBorder("transparent"))

  val Stack = createStackNavigator[Record[String, js.UndefOr[js.Object]]]()

  val Navigator = JsComponent[DefaultNavigatorOptions[js.Any, js.Any], Children.Varargs, Null](Stack.Navigator)

  @JSExportTopLevel("app")
  val app: StatelessFunctionalComponent[UnusedObject] = ScalaFnComponent[Props] { _ =>
    NavigationContainer.theme(theme)(
      Navigator(
        DefaultNavigatorOptions[js.Any, js.Any]()
          .setScreenOptions(js.Dynamic.literal(headerShown = false).asInstanceOf[js.Any])
          .setInitialRouteName("Home")
      )(
        Stack.Screen(
          js.Dynamic
            .literal(name = "Home", component = Home.component.raw)
            .asInstanceOf[typings.reactNavigationCore.typesMod.RouteConfig[
              typings.std.Record[String, scala.scalajs.js.UndefOr[scala.scalajs.js.Object]],
              String,
              typings.reactNavigationRouters.stackRouterMod.StackNavigationState[typings.std.Record[String, scala.scalajs.js.UndefOr[scala.scalajs.js.Object]]],
              typings.reactNavigationStack.typesMod.StackNavigationOptions,
              typings.reactNavigationStack.typesMod.StackNavigationEventMap
            ]]
        )
      )
    ).build
  }.toJsComponent.raw

}
