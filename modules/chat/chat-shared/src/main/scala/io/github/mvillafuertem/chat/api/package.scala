package io.github.mvillafuertem.chat

import sttp.tapir.{ Endpoint, EndpointInput, endpoint, _ }

package object api {

  // i n f o r m a t i o n
  private[api] lazy val apiResource:            String              = "api"
  private[api] lazy val apiVersion:             String              = "v1.0"
  private[api] lazy val apiNameResource:        String              = "api-resource"
  private[api] lazy val apiDescriptionResource: String              = "Api Resources"
  private[api] lazy val baseApiResource:        EndpointInput[Unit] = apiResource / apiVersion

  // e n d p o i n t
  private[api] lazy val baseEndpoint: Endpoint[Unit, Unit, Unit, Any] =
    endpoint
      .in(baseApiResource)
      .name(apiNameResource)
      .description(apiDescriptionResource)

  type AuthToken = String
  final lazy val AccessTokenHeaderName = "X-Auth-Token"

}
