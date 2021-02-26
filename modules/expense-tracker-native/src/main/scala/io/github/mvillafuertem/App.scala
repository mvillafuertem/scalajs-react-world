package io.github.mvillafuertem

import io.github.mvillafuertem.screens.Home
import japgolly.scalajs.react.{Children, JsComponent}
import japgolly.scalajs.react.component.JsFn.UnusedObject
import japgolly.scalajs.react.raw.React.StatelessFunctionalComponent
import typings.reactNavigationCore.anon.Component
import typings.reactNavigationCore.typesMod.{DefaultNavigatorOptions, EventMapBase, RouteConfig}
import typings.reactNavigationNative.components.NavigationContainer
import typings.reactNavigationNative.mod.DefaultTheme
import typings.reactNavigationRouters.typesMod.{NavigationState, ParamListBase}
import typings.reactNavigationStack.mod.createStackNavigator

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object App {

  val theme = DefaultTheme.setColors(DefaultTheme.colors.setBorder("transparent"))

  val Stack = createStackNavigator()


  private val Navigator = JsComponent[DefaultNavigatorOptions[js.Any,js.Any],  Children.Varargs, Null](Stack.Navigator)

  NavigationContainer.theme(theme)(
   Navigator(DefaultNavigatorOptions[js.Any, js.Any]()
     .setScreenOptions(js.Dynamic.literal(headerShown = false)
       .asInstanceOf[js.Any])
     .setInitialRouteName("Home"))(
     //Stack.Screen(js.Dynamic.literal.asInstanceOf[typings.reactNavigationCore.typesMod.RouteConfig[ParamListBase, String, NavigationState[ParamListBase], js.Object, EventMapBase]])
   )
  )

  @JSExportTopLevel("app")
  val app: StatelessFunctionalComponent[UnusedObject] = Home.component.toJsComponent.raw

}
