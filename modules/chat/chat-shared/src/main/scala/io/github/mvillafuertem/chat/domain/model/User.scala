package io.github.mvillafuertem.chat.domain.model

case class User(
  name:     String,
  email:    String,
  password: String,
  online:   Option[Boolean] = Some(false),
  uid:      Option[String] = None
)
