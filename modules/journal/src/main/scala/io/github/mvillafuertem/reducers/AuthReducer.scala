package io.github.mvillafuertem.reducers

import org.scalablytyped.runtime.StringDictionary
import typings.redux.mod.{Action, AnyAction}

import scala.scalajs.js

object AuthReducer {


  sealed trait Auth extends js.Object
  class Login()  extends js.Object


  def apply(state: js.UndefOr[String], action: Action[js.Any]): String = {
    println("ACTION   " + js.JSON.stringify(action))
    action.`type` match {
      case a: Login => ""
      case a => s"$a"
    }
  }

}
