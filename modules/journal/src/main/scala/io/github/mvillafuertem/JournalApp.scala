package io.github.mvillafuertem

import io.github.mvillafuertem.auth.LoginScreen
import io.github.mvillafuertem.store.store
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{Callback, CtorType, ReactEventFromInput, ScalaFnComponent}
import org.scalajs.dom.document
import typings.react.mod.useState
import typings.reactRedux.components.Provider
import zio.{App, ExitCode, IO, ZIO}

import scala.scalajs.js

object JournalApp extends App {

  object Main {

    val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
      Provider(store)(
        LoginScreen.component()
      ).build
    }
  }

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    IO.effectTotal {
      Main.component().renderIntoDOM(document.getElementById("container"))
    }.exitCode

}
