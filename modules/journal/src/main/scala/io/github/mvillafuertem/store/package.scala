package io.github.mvillafuertem

import io.github.mvillafuertem.reducers.AuthReducer.Reducer
import io.github.mvillafuertem.reducers.{AuthAction, AuthReducer, AuthState, UiAction, UiReducer, UiState}
import typings.redux.anon
import typings.redux.mod._
import typings.redux.mod.applyMiddleware
import typings.reduxThunk.reduxThunkRequire
import typings.reduxThunk.mod.default
import typings.reduxThunk.mod.ThunkDispatch
import typings.reduxDevtoolsExtension.developmentOnlyMod.composeWithDevTools
import typings.reduxDevtoolsExtension.mod.{EnhancerOptions, devToolsEnhancer}

import scala.scalajs.js
import scala.scalajs.js.{Dictionary, UndefOr}

package object store {


  private val value: js.Dictionary[Reducer[_ >: AuthState with UiState <: js.Object, _ >: AuthAction with UiAction <: Action[String]]] =
    js.Dictionary(
      "auth" -> Reducer,
      "ui" -> UiReducer.Reducer,
    )
  private val reducers: Reducer[CombinedState[StateFromReducersMapObject[Dictionary[Reducer[_ >: AuthState with UiState <: js.Object, _ >: AuthAction with UiAction <: Action[String]]]]], ActionFromReducersMapObject[Dictionary[Reducer[_ >: AuthState with UiState <: js.Object, _ >: AuthAction with UiAction <: Action[String]]]]] =
    combineReducers(value)

  val Store =
    createStore(AuthReducer.Reducer, composeWithDevTools(
      applyMiddleware(default),
    ))


}
