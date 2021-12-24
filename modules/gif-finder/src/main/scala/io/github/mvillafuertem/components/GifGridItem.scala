package io.github.mvillafuertem.components

import io.github.mvillafuertem.model.Gif
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.html._

@react object GifGridItem {

  case class Props(gif: Gif)

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { case Props(gif) =>
    div(className := "card animate__animated animate__fadeIn")(
      img(alt     := gif.title, src := gif.url),
      p(gif.title)
    )
  }

}
