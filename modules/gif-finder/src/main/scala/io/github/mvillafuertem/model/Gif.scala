package io.github.mvillafuertem.model

import scala.scalajs.js

class Gif(val id: String, val title: String, val url: String) extends js.Object

object Gif {
  def apply(id: String, title: String, url: String): Gif = new Gif(id, title, url)
}
