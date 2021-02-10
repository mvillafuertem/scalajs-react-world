package io.github.mvillafuertem.chat.domain.model

case class Message(
  sender:    String,
  recipient: String,
  body:      String
)
