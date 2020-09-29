package io.github.mvillafuertem

import typings.redux.mod.combineReducers
import typings.std.ReturnType

import scala.scalajs.js

package object reducers {

  type AppState = ReturnType[reducers.type]

  lazy val reducers = combineReducers(
    js.Dynamic.literal(
      authReducer = AuthReducer.default,
      uiReducer = UiReducer.default
    )
  )

}
