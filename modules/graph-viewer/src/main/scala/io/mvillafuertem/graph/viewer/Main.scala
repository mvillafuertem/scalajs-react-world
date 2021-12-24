package io.mvillafuertem.graph.viewer

import japgolly.scalajs.react.vdom.html_<^.{ <, _ }
import org.scalajs.dom

object Main {

  import japgolly.scalajs.react.vdom.html_<^._
  def main(args: Array[String]): Unit =
    japgolly.scalajs.react.vdom.html_<^.<.h1("hola-adios").renderIntoDOM(dom.document.getElementById("root"))
  // render(dom.document.getElementById("app"), h1("hola"))

}
