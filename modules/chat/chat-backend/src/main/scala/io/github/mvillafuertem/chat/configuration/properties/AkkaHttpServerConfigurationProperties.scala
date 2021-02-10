package io.github.mvillafuertem.chat.configuration.properties

import com.typesafe.config.{ Config, ConfigFactory }
import zio.Has

final case class AkkaHttpServerConfigurationProperties(
  interface: String,
  port:      Int
) {

  def withInterface(interface: String): AkkaHttpServerConfigurationProperties =
    copy(interface = interface)

  def withPort(port: Int): AkkaHttpServerConfigurationProperties =
    copy(port = port)

}

object AkkaHttpServerConfigurationProperties {

  type ZAkkaHttpServerConfigurationProperties = Has[AkkaHttpServerConfigurationProperties]

  def apply(config: Config = ConfigFactory.load().getConfig("infrastructure.http.server")): AkkaHttpServerConfigurationProperties =
    new AkkaHttpServerConfigurationProperties(
      interface = config.getString("interface"),
      port = config.getInt("port")
    )

}
