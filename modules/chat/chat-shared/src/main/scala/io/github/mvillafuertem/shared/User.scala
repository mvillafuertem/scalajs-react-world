package io.github.mvillafuertem.shared

case class User(
  name:     String,
  email:    String,
  password: String,
  online:   Option[Boolean] = Some(false)
)
