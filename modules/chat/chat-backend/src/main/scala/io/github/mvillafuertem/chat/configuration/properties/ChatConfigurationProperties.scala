package io.github.mvillafuertem.chat.configuration.properties

import com.typesafe.config.{ Config, ConfigFactory }
import zio.Has

final case class ChatConfigurationProperties(name: String) {

  def withName(name: String): ChatConfigurationProperties =
    copy(name = name)

}

object ChatConfigurationProperties {

  type ZChatConfigurationProperties = Has[ChatConfigurationProperties]

  def apply(config: Config = ConfigFactory.load().getConfig("application")): ChatConfigurationProperties =
    new ChatConfigurationProperties(
      name = config.getString("name")
    )

}
