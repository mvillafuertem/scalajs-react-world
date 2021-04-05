package io.github.mvillafuertem.domain

import scala.scalajs.js
import scala.util.Random

object Node {

  val colors: js.Array[String] = js.Array[String](
    "#69d2e7",
    "#a7dbd8",
    "#e0e4cc",
    "#f38630",
    "#fa6900",
    "#fe4365",
    "#fc9d9a",
    "#f9cdad",
    "#c8c8a9",
    "#83af9b",
    "#ecd078",
    "#d95b43",
    "#c02942",
    "#53777a"
  )

  def apply(id: Int, label: String, level: Int, group: Int = 0, shape: String = "dot", color: String = colors(Random.between(0, 13))): js.Dictionary[Any] =
    js.Dictionary(
      "id"    -> id,
      "label" -> label,
      "color" -> color,
      "level" -> level,
      "title" -> label,
      "group" -> group.toString,
      "shape" -> shape
    )

}
