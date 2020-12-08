package io.github.mvillafuertem

import io.github.mvillafuertem.css.BootstrapMinCSS
import io.github.mvillafuertem.css.ChatCSS
import io.github.mvillafuertem.css.LoginRegisterCSS
import io.github.mvillafuertem.router.AppRouter
import org.scalajs.dom.document
import zio.{ ExitCode, IO, ZIO }

object ChatApp extends zio.App {

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    IO.effectTotal {
      BootstrapMinCSS
      ChatCSS
      LoginRegisterCSS
      AppRouter.component().renderIntoDOM(document.getElementById("container"))
    }.exitCode

}
