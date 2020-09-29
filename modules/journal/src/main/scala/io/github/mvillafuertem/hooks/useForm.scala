package io.github.mvillafuertem.hooks

import io.github.mvillafuertem.model.Person
import japgolly.scalajs.react.{Callback, ReactEventFromInput}
import typings.react.mod.useState

import scala.scalajs.js

object useForm {

  def apply(initialState: Person = Person.default): (Person, js.Function1[ReactEventFromInput, Callback]) = {

    val js.Tuple2(state, setState) = useState(initialState)

    val handleInputChange: js.Function1[ReactEventFromInput, Callback] =
      (e: ReactEventFromInput) =>
        Callback {
          e.target.name match {
            case "email"    => setState(Person("", state.name, email = e.target.value, state.password))
            case "name"     => setState(Person("", name = e.target.value, state.email, state.password))
            case "password" => setState(Person("", state.name, state.email, password = e.target.value))
          }
        }

    (state, handleInputChange)

  }

}
