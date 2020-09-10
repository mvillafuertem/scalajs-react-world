package io.github.mvillafuertem.components

import org.scalajs.dom.Event
import org.scalajs.dom.raw.HTMLInputElement
import slinky.core.annotations.react
import slinky.core.facade.{Hooks, SetStateHookCallback}
import slinky.core.{FunctionalComponent, SyntheticEvent, TagElement}
import slinky.web.html._

@react object AddCategory {

  case class Props(setCategories: SetStateHookCallback[Seq[String]])

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { case Props(setCategories) =>
    val (inputValue, setInputValue) = Hooks.useState("")

    val handleInputChange: SyntheticEvent[TagElement#RefType, Event] => Unit =
      (e: SyntheticEvent[TagElement#RefType, Event]) => setInputValue(e.target.asInstanceOf[HTMLInputElement].value)

    val handleSubmit: SyntheticEvent[TagElement#RefType, Event] => Unit =
      (e: SyntheticEvent[TagElement#RefType, Event]) => {
        e.preventDefault()
        if (inputValue.nonEmpty) {
          setCategories(categories => Seq(inputValue) ++ categories )
        }
        setInputValue("")
      }

    form(onSubmit := handleSubmit)(
      input(
        `type` := "text",
        value := inputValue,
        onChange := handleInputChange
      )
    )

  }

}
