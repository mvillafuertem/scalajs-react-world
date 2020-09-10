package io.github.mvillafuertem.hooks

import io.github.mvillafuertem.model.Hero
import org.scalajs.dom.Event
import org.scalajs.dom.raw.HTMLInputElement
import slinky.core.{SyntheticEvent, TagElement}
import slinky.core.facade.Hooks.useState

import scala.scalajs.js

object useForm {

  def apply(initialState: String) = {
    val (state, setState) = useState(initialState)

    val handleInputChange: js.Function1[SyntheticEvent[TagElement#RefType, Event], Unit] =
      (e: SyntheticEvent[TagElement#RefType, Event]) =>
        e.target.asInstanceOf[HTMLInputElement].name match {
          case "searchText"    => setState(e.target.asInstanceOf[HTMLInputElement].value)
        }

    (state, handleInputChange)
  }

}