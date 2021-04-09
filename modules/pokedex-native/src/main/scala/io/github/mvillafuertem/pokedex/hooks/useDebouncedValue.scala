package io.github.mvillafuertem.pokedex.hooks

import typings.react.mod.{ useEffect, useState, Destructor, EffectCallback }

import scala.scalajs.js
import scala.scalajs.js.timers.{ clearTimeout, setTimeout }

object useDebouncedValue {

  def apply(input: String, time: Int = 500) = {

    val js.Tuple2(debounceValue, setDebounceValue) = useState(input)

    useEffect(
      { () =>
        val timeout = setTimeout(time)(setDebounceValue(input))

        (() => clearTimeout(timeout)): Destructor
      }: EffectCallback,
      js.Array(input)
    )

    debounceValue
  }

}
