package io.github.mvillafuertem

import io.github.mvillafuertem.store.store
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }
import org.scalajs.dom.document
import typings.reactRedux.components.Provider
import zio.{ App, ExitCode, IO, ZIO }

object JournalApp extends App {

  object Main {

    val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
      //Provider(store)(
        <.h1("JournalApp")
      //)
    }
  }

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    IO.effectTotal {
      Main.component().renderIntoDOM(document.getElementById("container"))
    }.exitCode

}
