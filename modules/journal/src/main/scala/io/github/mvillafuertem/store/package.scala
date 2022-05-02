package io.github.mvillafuertem

import io.github.mvillafuertem.actions.AppActions
import io.github.mvillafuertem.reducers._
import typings.redux.mod._
import typings.reduxDevtoolsExtension.mod.composeWithDevTools
import typings.reduxThunk.mod.{ default => Thunk, ThunkDispatch }

import scala.scalajs.js

package object store {

  val default = createStore(reducers)//, composeWithDevTools(applyMiddleware(Thunk)))

  val thunkDispatch = (appActions: AppActions) =>
    (thunkDispatch: ThunkDispatch[AppState, js.Any, AppActions], _: js.Function0[AppState], _: js.Any) => thunkDispatch(appActions)

}
