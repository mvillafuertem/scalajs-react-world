package io.github.mvillafuertem.chat.domain.model

case class Jwt(
  token:      String,
  expiration: Long,
  issuedAt:   Long
)
