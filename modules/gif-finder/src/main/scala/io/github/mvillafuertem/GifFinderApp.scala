package io.github.mvillafuertem

import org.scalajs.dom.document
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Fragment
import slinky.core.facade.Hooks.useState
import slinky.web.ReactDOM
import slinky.web.html.h2
import slinky.web.html.hr
import zio.{ App, ExitCode, IO, ZIO }
import io.github.mvillafuertem.components.GifGrid
import io.github.mvillafuertem.components.AddCategory

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("./App.css", JSImport.Default)
@js.native
object AppCSS extends js.Object

object GifFinderApp extends App {

  private val css = AppCSS

  @react object Main {

    type Props = Unit

    val component: FunctionalComponent[Props] = FunctionalComponent[Props] { _ =>
      val (categories, setCategories) = useState(Seq("Welcome"))

      Fragment()(
        h2("GifFinderApp"),
        AddCategory(setCategories),
        hr(),
        categories.map(category => GifGrid(category).withKey(java.util.UUID.randomUUID().toString))
      )

    }

  }

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    IO.effectTotal(
      ReactDOM
        .render(Main(), document.getElementById("container"))
    ).exitCode

}
