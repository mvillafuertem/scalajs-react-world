package io.github.mvillafuertem.actions

import io.github.mvillafuertem.facade.ReduxFacade.Extractor
import io.github.mvillafuertem.model.Person
import typings.redux.mod.Action

import scala.scalajs.js

@js.native
sealed trait AuthAction extends Action[String]

object AuthAction {

  @js.native
  trait Login extends AuthAction {
    val person: Person = js.native
  }

  object Login extends Extractor[Login] {
    protected val _type = "[Auth] Login"

    @scala.inline
    def apply(person: Person): Login = {
      val __obj = js.Dynamic.literal()
      __obj.updateDynamic("type")(_type.asInstanceOf[js.Any])
      __obj.asInstanceOf[Login].set("person", person)
    }

  }

  @js.native
  trait Logout extends AuthAction

  object Logout extends Extractor[Logout] {
    protected val _type = "[Auth] Logout"
  }

}
