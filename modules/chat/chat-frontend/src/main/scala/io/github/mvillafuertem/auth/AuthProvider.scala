package io.github.mvillafuertem.auth

import io.circe
import io.circe.generic.auto._
import io.circe.Json
import io.github.mvillafuertem.chat.domain.model.{Jwt, User}
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.VdomNode
import japgolly.scalajs.react.{AsyncCallback, Callback, CtorType, React, ScalaFnComponent}
import sttp.client.{FetchBackend, Identity, NothingT, RequestT, ResponseError, SttpBackend, basicRequest, _}
import sttp.model.MediaType
import typings.react.mod._
import typings.std.global.{console, localStorage}
import sttp.client.circe.{asJson, _}
import sttp.client.{SttpBackend, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

case class ChatState(
  user:       Option[User] = None,
  login:       Option[js.Function2[String, String, Unit]] = None,
  register:    Option[js.Function3[String, String, String, Unit]] = None,
  verifyToken: Option[Unit] = None,
  logout:      Option[js.Function0[Unit]] = None
)

object AuthContext {
  val value = React.createContext(ChatState())

}

object AuthProvider {


  val component: Component[VdomNode, CtorType.Props] = ScalaFnComponent[VdomNode] { children =>
    val js.Tuple2(auth, setAuth) = useState[User](User("", "", ""))

    implicit val sttpBackend: SttpBackend[Future, Nothing, NothingT] = FetchBackend()

    val login: js.Function2[String, String, Unit] = (email, password) => {

      val url = "http://localhost:8080/api/v1/auth/login"

      val requestPOST: RequestT[Identity, Either[ResponseError[circe.Error], Jwt], Nothing] = basicRequest
        .post(uri"$url")
        .contentType(MediaType.ApplicationJson)
        .response(asJson[Jwt])
        .body(s"""{"email":"$email","name": "$email","password": "$password"}""")

      def result: Future[Either[ResponseError[circe.Error], Jwt]] = requestPOST.send().map(_.body.map{b => localStorage.setItem("token", b.token); b})
      result.map(console.log(_))

      setAuth(User(email, email, "", Some(true), Some("123")))

    }

    val register: js.Function3[String, String, String, Unit] = (name, email, password) => {

      val url = "http://localhost:8080/api/v1/auth/login/new"

      val requestPOST: RequestT[Identity, Either[ResponseError[circe.Error], Json], Nothing] = basicRequest
        .post(uri"$url")
        .contentType(MediaType.ApplicationJson)
        .response(asJson[Json])
        .body(s"""{"email":"$email","name": "$name","password": "$password"}""")

      def result: Future[Either[ResponseError[circe.Error], String]] = requestPOST.send().map(_.body.map(_.noSpaces))
      result.map(console.log(_))
    }

    val verifyToken: Unit = useCallback[Unit](() => (), js.Array[js.Any]())

    val logout: js.Function0[Unit] = () => ()

    AuthContext.value.provide(ChatState(Some(auth), Some(login), Some(register), Some(verifyToken), Some(logout)))(children)
  }

}
