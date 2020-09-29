package io.github.mvillafuertem

import io.github.mvillafuertem.reducers._
import typings.redux.mod._
import typings.reduxDevtoolsExtension.mod.{ devToolsEnhancer, EnhancerOptions }

package object store {

  val default = createStore(reducers, devToolsEnhancer(EnhancerOptions().setName("Journal Store")))

}
