package io.github.mvillafuertem

import io.github.mvillafuertem.reducers.AuthReducer
import io.github.mvillafuertem.reducers.AuthReducer.Auth
import typings.redux.mod._
import typings.reduxDevtoolsExtension.developmentOnlyMod.composeWithDevTools

import scala.scalajs.js

package object store {

  private val function: Reducer[String, Action[js.Any]] = AuthReducer.apply _

  val initial: PreloadedState[String] = "".asInstanceOf[PreloadedState[String]]

  private val composeEnhancers: StoreEnhancer[Nothing, js.Object] = composeWithDevTools()

  val store: Store[String, Action[js.Any]] = createStore.apply(function, initial, composeEnhancers)

}
