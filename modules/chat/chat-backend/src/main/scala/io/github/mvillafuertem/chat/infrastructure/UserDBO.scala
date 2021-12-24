package io.github.mvillafuertem.chat.infrastructure

import org.mongodb.scala.bson.ObjectId

case class UserDBO(
  name:     String,
  email:    String,
  password: String,
  online:   Option[Boolean] = Some(false),
  // @BsonProperty("_id") _id: Option[ObjectId] = None
  _id: Option[ObjectId] = None
)
