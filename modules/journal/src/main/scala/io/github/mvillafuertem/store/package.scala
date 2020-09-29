package io.github.mvillafuertem

import io.github.mvillafuertem.reducers._
import typings.redux.mod._
import typings.reduxDevtoolsExtension.mod.composeWithDevTools

package object store {

  val default = createStore(reducers, composeWithDevTools(applyMiddleware(typings.reduxThunk.mod.default)))

}
