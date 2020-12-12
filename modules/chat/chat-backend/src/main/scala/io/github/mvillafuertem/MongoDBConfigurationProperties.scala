package io.github.mvillafuertem

import com.typesafe.config.{ Config, ConfigFactory }

final case class MongoDBConfigurationProperties(user: String, password: String, hostname: String, port: Int, database: String) {

  def withUser(user: String): MongoDBConfigurationProperties =
    copy(user = user)

  def withPassword(password: String): MongoDBConfigurationProperties =
    copy(password = password)

  def withHostname(hostname: String): MongoDBConfigurationProperties =
    copy(hostname = hostname)

  def withPort(port: Int): MongoDBConfigurationProperties =
    copy(port = port)

  def withDatabase(database: String): MongoDBConfigurationProperties =
    copy(database = database)

}

object MongoDBConfigurationProperties {

  val default: Config = ConfigFactory.load().getConfig("infrastructure.mongodb")

  def apply(config: Config = default): MongoDBConfigurationProperties =
    new MongoDBConfigurationProperties(
      user = config.getString("user"),
      password = config.getString("password"),
      hostname = config.getString("hostname"),
      port = config.getInt("port"),
      database = config.getString("database")
    )

}
