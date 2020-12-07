package io.github.mvillafuertem

import io.github.mvillafuertem.shared.SharedMessages
import org.scalajs.dom
import zio.{ ExitCode, IO, ZIO }

object ChatApp extends zio.App {

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    IO.effectTotal {
      //LoginRegisterCSS
      //AppRouter.component().renderIntoDOM(document.getElementById("container"))
      dom.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
    }.exitCode

}
