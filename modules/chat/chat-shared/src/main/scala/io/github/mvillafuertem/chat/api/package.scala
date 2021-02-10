package io.github.mvillafuertem.chat

import sttp.tapir.{ Endpoint, EndpointInput, endpoint, _ }

package object api {

  // i n f o r m a t i o n
  private[api] lazy val apiResource:            String              = "api"
  private[api] lazy val apiVersion:             String              = "v1"
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

  // PartialServerEndpoint
//  /**
//   * Defines authentication method.
//   *
//   * Returns either String with error message in case of failure or Api Key if authenticated successfully
//   **/
//  def authenticate(token: Option[String]): Future[Either[String, AuthToken]]
//
//  /** * Definition of the partial server endpoint which adds authentication to the endpoint */
//  val securedEndpoint: PartialServerEndpoint[AuthToken, Unit, String, Unit, Nothing, F] = endpoint
//    .in(header[AuthToken](AccessTokenHeaderName))
//    .errorOut(statusCode(Unauthorized).and(stringBody.description("An error message when authentication failed")))
//    .serverLogicForCurrent(authenticate)

}
