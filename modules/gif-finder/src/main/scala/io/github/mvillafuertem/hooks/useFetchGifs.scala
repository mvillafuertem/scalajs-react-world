package io.github.mvillafuertem.hooks

import io.circe.optics.JsonPath.root
import io.github.mvillafuertem.model.Gif
import slinky.core.facade.Hooks.{ useEffect, useState }

import scala.concurrent.ExecutionContext.Implicits.global

object useFetchGifs {

  def apply(category: String) = {
    val (state, setState) = useState((Seq.empty[Gif], true))

    useEffect(
      () =>
        getGifts(category)
          .map(
            _.map(root.data.each.json.getAll)
              .map(
                _.map(json =>
                  Gif(
                    root.id.string.getOption(json).getOrElse(""),
                    root.title.string.getOption(json).getOrElse(""),
                    root.images.downsized_medium.url.string.getOption(json).getOrElse("")
                  )
                )
              )
              .map(setState(_, false))
          ),
      Seq(category)
    )
    state
  }

}
