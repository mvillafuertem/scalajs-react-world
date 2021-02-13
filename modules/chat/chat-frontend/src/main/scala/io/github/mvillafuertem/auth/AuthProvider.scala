package io.github.mvillafuertem.auth

import io.circe
import io.circe.Json
import io.circe.generic.auto._
import io.github.mvillafuertem.auth.AuthContext.value.Provider
import io.github.mvillafuertem.chat.domain.model.{ Jwt, User }
import japgolly.scalajs.react.component.JsFn
import japgolly.scalajs.react.component.JsFn.Unmounted
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.{ TagMod, VdomElement, VdomNode }
import japgolly.scalajs.react.{ raw, AsyncCallback, Children, CtorType, JsFnComponent, ScalaFnComponent }
import sttp.client3.circe.asJson
import sttp.client3.{ basicRequest, FetchBackend, Identity, RequestT, ResponseException, SttpBackend, UriContext }
import sttp.model.MediaType
import typings.react.mod._
import typings.std.global.{ console, localStorage }
import typings.sweetalert2.mod.{ SweetAlertIcon, default => Swal }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{ Failure, Success }

case class ChatState(
  user:        Option[User] = None,
  login:       Option[js.Function2[String, String, Unit]] = None,
  register:    Option[js.Function3[String, String, String, Unit]] = None,
  verifyToken: Option[js.Function1[Unit, Unit]] = None,
  logout:      Option[js.Function0[Unit]] = None
)

object AuthContext {

  val value: Context[ChatState] = createContext(null)

}

object AuthProvider {

  val component: Component[VdomNode, CtorType.Props] = ScalaFnComponent[VdomNode] { children =>
    val js.Tuple2(auth, setAuth) = useState[Option[User]](None)

    val sttpBackend: SttpBackend[Future, Any] = FetchBackend()

    val login: js.Function2[String, String, Unit] = (email, password) => {

      val url = "http://localhost:8080/api/v1/auth/login"

      val requestPOST = basicRequest
        .post(uri"$url")
        .contentType(MediaType.ApplicationJson)
        .response(asJson[Jwt])
        .body(s"""{"email":"$email","name": "$email","password": "$password"}""")

      def result: Future[Either[ResponseException[String, circe.Error], Jwt]] =
        requestPOST.send(sttpBackend).map(_.body.map { b => localStorage.setItem("token", b.token); b })

      AsyncCallback
        .fromFuture(result.transformWith {
          case Failure(exception) =>
            console.log(exception)
            Swal.fire("Error", "Verifique el email o contraseña", SweetAlertIcon.error).toFuture
          case Success(value) =>
            console.log(value)
            Future.successful(setAuth(Some(User(email, email, "", Some(true), Some("123")))))

        })
        .runNow()

    }

    val register: js.Function3[String, String, String, Unit] = (name, email, password) => {

      val url = "http://localhost:8080/api/v1/auth/login/new"

      val requestPOST: RequestT[Identity, Either[ResponseException[String, circe.Error], Json], Any] = basicRequest
        .post(uri"$url")
        .contentType(MediaType.ApplicationJson)
        .response(asJson[Json])
        .body(s"""{"email":"$email","name": "$name","password": "$password"}""")

      def result: Future[Either[ResponseException[String, circe.Error], String]] = requestPOST.send(sttpBackend).map(_.body.map(_.noSpaces))
      AsyncCallback
        .fromFuture(result.transformWith {
          case Failure(exception) =>
            console.log(exception)
            Swal.fire("Error", "El usuario ya existe", SweetAlertIcon.error).toFuture
          case Success(value) =>
            console.log(value)
            Future.successful(setAuth(Some(User(email, email, "", Some(true), Some("123")))))

        })
        .runNow()
    }

    val verifyToken: js.Function1[Unit, Unit] =
      useCallback[js.Function1[Unit, Unit]](
        _ => {

          console.log("PEPEPEPEPEP")

          val token = localStorage.getItem("token")

          if (token.isEmpty) {
            setAuth(None)
          }

          val url = "http://localhost:8080/api/v1/auth/login/renew"

          val requestGET: RequestT[Identity, Either[ResponseException[String, circe.Error], Json], Any] = basicRequest
            .get(uri"$url")
            .contentType(MediaType.ApplicationJson)
            .response(asJson[Json])

          def result: Future[Either[ResponseException[String, circe.Error], String]] = requestGET.send(sttpBackend).map(_.body.map(_.noSpaces))
          AsyncCallback
            .fromFuture(result.transformWith {
              case Failure(exception) =>
                console.log(exception)
                Future.successful(setAuth(None))
              case Success(value) =>
                console.log(value)
                Future.successful(setAuth(Some(User("email", "email", "", Some(true), Some("123")))))

            })
            .runNow()

        },
        js.Array[js.Any]()
      )

    val logout: js.Function0[Unit] = () => ()

    lazy val ProviderComponent: JsFn.Component[ProviderProps[ChatState], CtorType.Props] =
      JsFnComponent.force[ProviderProps[ChatState], Children.None](AuthContext.value.Provider)

    val value: Unmounted[ProviderProps[ChatState]] = ProviderComponent(
      ProviderProps(
        ChatState(
          auth,
          Some(login),
          Some(register),
          Some(verifyToken),
          Some(logout)
        )
      ).setChildren(children)
    )

    value
  //AuthContext.value.setProvider(value)
  }

}
