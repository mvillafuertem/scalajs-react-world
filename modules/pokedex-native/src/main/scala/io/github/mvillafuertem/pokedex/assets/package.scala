package io.github.mvillafuertem.pokedex

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

package object assets {

  @js.native
  @JSImport("../../../assets/pokebola.png", JSImport.Default)
  object PokebolaPNG extends js.Object

  @js.native
  @JSImport("../../../assets/pokebola-blanca.png", JSImport.Default)
  object PokebolaBlancaPNG extends js.Object

}
