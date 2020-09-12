package io.github.mvillafuertem

import typings.redux.mod.{ combineReducers, createStore }

import scala.scalajs.js

package object store {

  val reducers = combineReducers(js.Dynamic.literal(auth = ""))

  val store = createStore(reducers)

}
