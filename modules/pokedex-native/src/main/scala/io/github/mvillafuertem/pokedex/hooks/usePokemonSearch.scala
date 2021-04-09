package io.github.mvillafuertem.pokedex.hooks

import io.github.mvillafuertem.pokedex.api.PokemonApi
import io.github.mvillafuertem.pokedex.hooks.usePokemonPaginated.{PokemonPaginatedResponse, SimplePokemon, mapPokemonList}
import japgolly.scalajs.react.AsyncCallback
import japgolly.scalajs.react.raw.React
import org.scalablytyped.runtime.StringDictionary
import typings.react.mod._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.UndefOr

object usePokemonSearch {

  def apply() = {

    val js.Tuple2(isFetching, setIsFetching) = useState(true)
    val js.Tuple2(simplePokemonList, setSimplePokemonList) = useState(js.Array[SimplePokemon]())

    val loadPokemons = () =>
      AsyncCallback.fromFuture {
        val resp = PokemonApi.api.get[Any, PokemonPaginatedResponse]("https://pokeapi.co/api/v2/pokemon?limit=1200").toFuture
        resp
          .map(_.asInstanceOf[js.Dynamic].data.asInstanceOf[PokemonPaginatedResponse])
          .map { r =>
            mapPokemonList(r.results)
          }
          .map { newList =>
            {
              setSimplePokemonList(newList)
              setIsFetching(false)
            }
          }
      }

    useEffect(
      (() => {
        loadPokemons().runNow()
      }):         EffectCallback,
      js.Array(): DependencyList
    )

    (isFetching, simplePokemonList)

  }

}
