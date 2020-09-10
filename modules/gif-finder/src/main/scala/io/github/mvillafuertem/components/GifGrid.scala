package io.github.mvillafuertem.components

import io.github.mvillafuertem.hooks.useFetchGifs
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Fragment
import slinky.web.html.{ className, div, h3, p }

@react object GifGrid {

  case class Props(category: String)

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { case Props(category) =>
    val (gifs, loading) = useFetchGifs(category)

    Fragment(
      h3(className := "animate__animated animate__fadeIn")(category),
      if (loading) p(className := "animate__animated animate__flash")("Loading...")
      else
        div(className := "card-grid")(
          gifs.map(gif => GifGridItem(gif).withKey(gif.id))
        )
    )

  }

}
