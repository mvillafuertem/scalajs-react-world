package io.github.mvillafuertem

import io.github.mvillafuertem.reducers._
import typings.redux.mod._
import typings.reduxDevtoolsExtension.mod.composeWithDevTools
import typings.reduxThunk.mod.{ default => Thunk }

package object store {

  val default = createStore(reducers, composeWithDevTools(applyMiddleware(Thunk)))

}
