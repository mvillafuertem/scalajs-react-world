package io.github.mvillafuertem.facade

import typings.redux.mod.Action

import scala.scalajs.js

/**
 * This is very rudimentary, just enough to support the demo
 */
object ReduxFacade {

  /**
   * Since redux forces us to use plain js objects, this is the only trivially extractable boilerplate
   */
  trait Extractor[T] {

    protected val _type: String

    def unapply(a: Action[String]): Option[T] =
      if (a.`type` == _type) Some(a.asInstanceOf[T]) else None

    @scala.inline
    def apply(): T = {
      val __obj = js.Dynamic.literal()
      __obj.updateDynamic("type")(_type.asInstanceOf[js.Any])
      __obj.asInstanceOf[T]
    }
  }

}
