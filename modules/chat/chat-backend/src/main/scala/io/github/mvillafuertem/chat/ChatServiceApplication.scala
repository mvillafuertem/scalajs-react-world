package io.github.mvillafuertem.chat

import io.github.mvillafuertem.chat.configuration.ChatServiceConfiguration
import zio.{ ExitCode, ZIO }

/**
 * @author
 *   Miguel Villafuerte
 */
object ChatServiceApplication extends ChatServiceConfiguration with zio.App {

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] = chatServiceApplication

}
