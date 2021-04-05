package io.github.mvillafuertem.domain

import scala.scalajs.js

object Edge {

  def apply(from: Int, to: Int, arrows: String = "to"): js.Dictionary[Any] =
    js.Dictionary("from" -> from, "to" -> to, "arrows" -> arrows)

}
