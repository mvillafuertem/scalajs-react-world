package io.github.mvillafuertem.chat.api

import io.circe.generic.auto._
import io.github.mvillafuertem.shared.User
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.model.UsernamePassword

trait AuthEndpoint {

  val loginUser: Endpoint[Unit, String, User, Any] =
    baseEndpoint.post
      .in("auth" / "login")
      .errorOut(stringBody)
      .out(jsonBody[User])

  val newUser: Endpoint[User, String, User, Any] =
    baseEndpoint.post
      .in("auth" / "login" / "new")
      .in(jsonBody[User])
      .errorOut(stringBody)
      .out(jsonBody[User])

  val renewToken: Endpoint[Unit, String, User, Any] =
    baseEndpoint.get
      .in("auth" / "login" / "renew")
      .errorOut(stringBody)
      .out(jsonBody[User])

}

object AuthEndpoint extends AuthEndpoint
