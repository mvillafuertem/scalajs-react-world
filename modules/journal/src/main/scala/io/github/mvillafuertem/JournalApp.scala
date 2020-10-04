package io.github.mvillafuertem

import io.github.mvillafuertem.components.auth.{LoginScreen, RegisterScreen}
import io.github.mvillafuertem.routers.AppRouter
import org.scalajs.dom.document
import typings.reactRedux.components.Provider
import zio.{App, ExitCode, IO, ZIO}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object JournalApp extends App {

  @JSImport("./styles/styles.scss", JSImport.Namespace)
  @js.native
  object JournalCSS extends js.Object

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    IO.effectTotal {
      JournalCSS
      Provider(store.default)(
        AppRouter.component()
      ).renderIntoDOM(document.getElementById("container"))
    }.exitCode

}
