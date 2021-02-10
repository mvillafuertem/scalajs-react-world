package io.github.mvillafuertem

import io.github.mvillafuertem.auth.AuthProvider
import io.github.mvillafuertem.css.{BootstrapMinCSS, ChatCSS, FontAwesomeCSS, LoginRegisterCSS}
import io.github.mvillafuertem.router.AppRouter
import org.scalajs.dom.document
import zio.{ExitCode, IO, ZIO}

import scala.scalajs.js.annotation.JSExportTopLevel

object ChatApp extends zio.App {

  @JSExportTopLevel("main")
  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    IO.effectTotal {
      BootstrapMinCSS
      FontAwesomeCSS
      ChatCSS
      LoginRegisterCSS
      AuthProvider.component(AppRouter.component()).renderIntoDOM(document.getElementById("container"))
    }.exitCode

}
