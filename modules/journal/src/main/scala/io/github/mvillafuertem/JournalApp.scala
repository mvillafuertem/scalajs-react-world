package io.github.mvillafuertem

import io.github.mvillafuertem.components.auth.{LoginScreen, RegisterScreen}
import io.github.mvillafuertem.routers.AppRouter
import org.scalajs.dom.document
import typings.reactRedux.components.Provider
import zio.{App, ExitCode, IO, ZIO}

import scala.scalajs.js

object JournalApp extends App {

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    IO.effectTotal {
      Provider(store.default)(
        AppRouter.component()
        //LoginScreen.connectElem((new js.Object).asInstanceOf[LoginScreen.Props])()
        //RegisterScreen.component()
      ).renderIntoDOM(document.getElementById("container"))
    }.exitCode

}
