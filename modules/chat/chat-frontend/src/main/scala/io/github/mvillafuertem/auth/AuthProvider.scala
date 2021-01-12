package io.github.mvillafuertem.auth

import io.circe
import io.circe.Json
import io.github.mvillafuertem.chat.domain.model.User
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.VdomNode
import japgolly.scalajs.react.{ CtorType, React, ScalaFnComponent }
import sttp.client.circe.asJson
import sttp.client.{ FetchBackend, Identity, NothingT, RequestT, ResponseError, SttpBackend, basicRequest, _ }
import typings.react.mod._
import typings.std.global.console

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

case class ChatState(
  login:       Option[js.Function2[String, String, Unit]] = None,
  register:    Option[js.Function3[String, String, String, Unit]] = None,
  verifyToken: Option[Unit] = None,
  logout:      Option[js.Function0[Unit]] = None
)

object AuthContext {
  def apply() = React.createContext(ChatState())

}

object AuthProvider {


  val component: Component[VdomNode, CtorType.Props] = ScalaFnComponent[VdomNode] { children =>
    val js.Tuple2(auth, setAuth) = useState[User](User("", "", ""))

    implicit val sttpBackend: SttpBackend[Future, Nothing, NothingT] = FetchBackend()

    val login: js.Function2[String, String, Unit] = (email, password) => {

      val url = "http://localhost:8080/api/v1/auth/login/new"

      val requestPOST: RequestT[Identity, Either[ResponseError[circe.Error], Json], Nothing] = basicRequest
        .post(uri"$url")
        //.contentType(MediaType.ApplicationJson)
        .body("email" -> email, "password" -> password)
        .response(asJson[Json])

      def result: Future[Either[ResponseError[circe.Error], Json]] = requestPOST.send().map(_.body)

      console.log(result)
    }

    val register: js.Function3[String, String, String, Unit] = (name, email, password) => ()

    val verifyToken: Unit = useCallback[Unit](() => (), js.Array[js.Any]())

    val logout: js.Function0[Unit] = () => ()

    AuthContext().provide(ChatState(Some(login), Some(register), Some(verifyToken), Some(logout)))(children)
  }

}
