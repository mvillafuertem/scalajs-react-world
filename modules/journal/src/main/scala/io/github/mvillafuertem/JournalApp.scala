package io.github.mvillafuertem

import io.github.mvillafuertem.auth.LoginScreen
import io.github.mvillafuertem.store.{Store, connectElem}
import org.scalajs.dom.document
import typings.reactRedux.components.Provider
import zio.{App, ExitCode, IO, ZIO}

import scala.scalajs.js

object JournalApp extends App {

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    IO.effectTotal {
      Provider(Store)(
        connectElem({
          val props = (new js.Object).asInstanceOf[LoginScreen.Props]
          props
        })()
      ).renderIntoDOM(document.getElementById("container"))
    }.exitCode

}
