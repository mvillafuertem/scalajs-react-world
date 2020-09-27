package io.github.mvillafuertem

import io.github.mvillafuertem.reducers.AuthReducer.Reducer
import io.github.mvillafuertem.reducers._
import typings.redux.mod.{ applyMiddleware, _ }
import typings.reduxDevtoolsExtension.mod.{ devToolsEnhancer, EnhancerOptions }
import typings.reduxThunk.mod.default
//import typings.reduxDevtoolsExtension.mod.composeWithDevTools
import typings.reduxDevtoolsExtension.mod.composeWithDevTools

import scala.scalajs.js
import scala.scalajs.js.Dictionary

package object store {

  private val value: js.Dictionary[Reducer[_ >: AuthState with UiState <: js.Object, _ >: AuthAction with UiAction <: Action[String]]]            =
    js.Dictionary(
      "auth" -> Reducer,
      "ui"   -> UiReducer.Reducer
    )
  private val reducers: Reducer[CombinedState[
    StateFromReducersMapObject[Dictionary[Reducer[_ >: AuthState with UiState <: js.Object, _ >: AuthAction with UiAction <: Action[String]]]]
  ], ActionFromReducersMapObject[Dictionary[Reducer[_ >: AuthState with UiState <: js.Object, _ >: AuthAction with UiAction <: Action[String]]]]] =
    combineReducers(value)

//  val StoreMiddleware =
//    createStore(
//      AuthReducer.Reducer,
//      composeWithDevTools(
//        applyMiddleware(default)
//      )
//    )

  val Store =
    createStore(AuthReducer.Reducer, devToolsEnhancer(EnhancerOptions().setName("Journal Store")))

}
