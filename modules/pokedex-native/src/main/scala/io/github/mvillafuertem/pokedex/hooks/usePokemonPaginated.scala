package io.github.mvillafuertem.pokedex.hooks

import io.github.mvillafuertem.pokedex.api.PokemonApi
import japgolly.scalajs.react.{ AsyncCallback, ScalaFnComponent }
import japgolly.scalajs.react.raw.React
import org.scalablytyped.runtime.StringDictionary
import typings.react.mod.{ useEffect, useRef, useState, DependencyList, EffectCallback }
import typings.reactNative.mod.global.console

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.UndefOr

object usePokemonPaginated {

  trait PokemonPaginatedResponse extends js.Object {
    val count:    Int
    val next:     String
    val previous: Null
    val results:  js.Array[Result]
  }

  trait Result extends js.Object {
    val name: String
    val url:  String
  }

  trait SimplePokemon extends js.Object {
    val id:      String
    val name:    String
    val picture: String
    val color:   js.UndefOr[String]
  }

  trait PokemonFull extends js.Object {
    val abilities:                js.Array[Ability]
    val base_experience:          Int
    val forms:                    js.Array[Species]
    val game_indices:             js.Array[GameIndex]
    val height:                   Int
    val held_items:               js.Array[Any]
    val id:                       Int
    val is_default:               Boolean
    val location_area_encounters: String
    val moves:                    js.Array[Move]
    val name:                     String
    val order:                    Int
    val past_types:               js.Array[Any]
    val species:                  Species
    val sprites:                  Sprites
    val stats:                    js.Array[Stat]
    val types:                    js.Array[Type]
    val weight:                   Int
  }

  trait Ability extends js.Object {
    val ability:   Species
    val is_hidden: Boolean
    val slot:      Int
  }

  trait Species extends js.Object {
    val name: String
    val url:  String
  }

  trait GameIndex extends js.Object {
    val game_index: Int
    val version:    Species
  }

  trait Move extends js.Object {
    val move:                  Species
    val version_group_details: js.Array[VersionGroupDetail]
  }

  trait VersionGroupDetail extends js.Object {
    val level_learned_at:  Int
    val move_learn_method: Species
    val version_group:     Species
  }

  trait GenerationV extends js.Object {
    val `black-white`: Sprites
  }

  trait GenerationIv extends js.Object {
    val `diamond-pearl`:        Sprites
    val `heartgold-soulsilver`: Sprites
    val platinum:               Sprites
  }

  trait Versions extends js.Object {
    val `generation-i`:    GenerationI
    val `generation-ii`:   GenerationIi
    val `generation-iii`:  GenerationIii
    val `generation-iv`:   GenerationIv
    val `generation-v`:    GenerationV
    val `generation-vi`:   StringDictionary[GenerationVi]
    val `generation-vii`:  GenerationVii
    val `generation-viii`: GenerationViii
  }

  trait Sprites extends js.Object {
    val back_default:       String
    val back_female:        Null
    val back_shiny:         String
    val back_shiny_female:  Null
    val front_default:      String
    val front_female:       Null
    val front_shiny:        String
    val front_shiny_female: Null
    val other:              js.UndefOr[Other]
    val versions:           js.UndefOr[Versions]
    val animated:           js.UndefOr[Sprites]
  }

  trait GenerationI extends js.Object {
    val `red-blue`: RedBlue
    val yellow:     RedBlue
  }

  trait RedBlue extends js.Object {
    val back_default:  String
    val back_gray:     String
    val front_default: String
    val front_gray:    String
  }

  trait GenerationIi extends js.Object {
    val crystal: Crystal
    val gold:    Crystal
    val silver:  Crystal
  }

  trait Crystal extends js.Object {
    val back_default:  String
    val back_shiny:    String
    val front_default: String
    val front_shiny:   String
  }

  trait GenerationIii extends js.Object {
    val emerald:             Emerald
    val `firered-leafgreen`: Crystal
    val `ruby-sapphire`:     Crystal
  }

  trait Emerald extends js.Object {
    val front_default: String
    val front_shiny:   String
  }

  trait GenerationVi extends js.Object {
    val front_default:      String
    val front_female:       Null
    val front_shiny:        String
    val front_shiny_female: Null
  }

  trait GenerationVii extends js.Object {
    val icons:                  DreamWorld
    val `ultra-sun-ultra-moon`: GenerationVi
  }

  trait DreamWorld extends js.Object {
    val front_default: String
    val front_female:  Null
  }

  trait GenerationViii extends js.Object {
    val icons: DreamWorld
  }

  trait Other extends js.Object {
    val dream_world:        DreamWorld
    val `official-artwork`: OfficialArtwork
  }

  trait OfficialArtwork extends js.Object {
    val front_default: String
  }

  trait Stat extends js.Object {
    val base_stat: Int
    val effort:    Int
    val stat:      Species
  }

  trait Type extends js.Object {
    val slot:   Int
    val `type`: Species
  }

  val mapPokemonList: js.Array[Result] => js.Array[SimplePokemon] = (pokemonList: js.Array[Result]) =>
    pokemonList.map(result =>
      new SimplePokemon {
        private val urlParts: Array[String]   = result.url.split("/")
        override val id:      String          = urlParts(urlParts.length - 1) // tail
        override val name:    String          = result.name
        override val picture: String          = s"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
        override val color:   UndefOr[String] = js.undefined

      }
    )

  def apply() = {

    val js.Tuple2(isMounted, setIsMounted) = useState(true)

    val js.Tuple2(isLoading, setIsLoading)                 = useState(true)
    val js.Tuple2(simplePokemonList, setSimplePokemonList) = useState(js.Array[SimplePokemon]())
    val nextUrl: React.RefHandle[String] = useRef("https://pokeapi.co/api/v2/pokemon?limit=20")

    val loadPokemons = () =>
      AsyncCallback.fromFuture {
        setIsLoading(true)
        val resp = PokemonApi.api.get[Any, PokemonPaginatedResponse](nextUrl.current.asInstanceOf[String]).toFuture
        nextUrl.current.asInstanceOf[String]
        resp
          .map(_.asInstanceOf[js.Dynamic].data.asInstanceOf[PokemonPaginatedResponse])
          .map { r =>
            nextUrl.current = r.next
            mapPokemonList(r.results)
          }
          .map { newList =>
            setSimplePokemonList(simplePokemonList ++ newList)
            setIsLoading(false)
          }
      }

    useEffect(
      (() => loadPokemons().runNow()): EffectCallback,
      js.Array():                      DependencyList
    )

    (isLoading, simplePokemonList, loadPokemons)

  }

}
