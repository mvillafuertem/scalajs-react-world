package io.github.mvillafuertem.pokedex.api

import typings.axios.mod
import typings.axios.mod.default.{^ => axios}


object PokemonApi {


  val api: mod.AxiosInstance = axios.create()


}
