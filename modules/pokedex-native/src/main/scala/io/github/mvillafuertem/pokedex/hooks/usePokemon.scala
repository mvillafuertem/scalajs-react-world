package io.github.mvillafuertem.pokedex.hooks

import io.github.mvillafuertem.pokedex.api.PokemonApi
import io.github.mvillafuertem.pokedex.hooks.usePokemonPaginated.PokemonFull
import japgolly.scalajs.react.AsyncCallback
import typings.react.mod.{ useEffect, useState, EffectCallback }
import typings.reactNative.mod.global.console

import scala.scalajs.js
import scala.scalajs.js.|

object usePokemon {

  def apply(id: String): (Boolean, PokemonFull) = {

    val js.Tuple2(isLoading, setIsLoading) = useState(true)
    val js.Tuple2(pokemon, setPokemon) =
      useState[PokemonFull](js.Object.asInstanceOf[PokemonFull]) // {} as PokemonFull (syntax TypeScript) esto devuelve undefined y no un error

    val loadPokemon = () =>
      AsyncCallback.fromJsPromise {

        val response = PokemonApi.api
          .get[js.Any, js.Dynamic](s"https://pokeapi.co/api/v2/pokemon/$id")
          .`then`[Unit]((respone: js.Dynamic) => setPokemon(respone.data.asInstanceOf[PokemonFull]))
          .`then`[Unit]((_ => setIsLoading(false)): js.Function1[Unit, Unit | scala.scalajs.js.Thenable[Unit]])

        response

      }.runNow()

    useEffect(
      (() => loadPokemon()): EffectCallback,
      js.Array[js.Any]() // Important!! esto evita que entre en bucle
    )

    (isLoading, pokemon)

  }

}
