package io.github.mvillafuertem.reducers

import io.github.mvillafuertem.hooks.Person
import io.github.mvillafuertem.reducers.AuthAction.{Login, Logout}
import typings.redux.mod.{ActionFromReducersMapObject, CombinedState, Reducer, StateFromReducersMapObject}

import scala.scalajs.js
import scala.scalajs.js.Dictionary

object AuthReducer {


 // Reducer[CombinedState[StateFromReducersMapObject[Reducer[js.Dictionary[String], AuthAction]]], ActionFromReducersMapObject[Reducer[AuthState, AuthAction]]]
 //Reducer[CombinedState[StateFromReducersMapObject[js.Dictionary[Reducer[AuthState, AuthAction]]]], ActionFromReducersMapObject[Reducer[AuthState, AuthAction]]]


  val Reducer: Reducer[AuthState, AuthAction] = (stateOpt, action) => {
    val state = stateOpt.getOrElse(AuthState.initial)
    println(js.JSON.stringify(action))
    action match {
      case Login(_action) => AuthState(_action.person)
      case Logout(_) => AuthState(Person.default)
      case _ => state
    }
  }

}
