package io.github.mvillafuertem.auth

import io.circe
import io.circe.generic.auto._
import io.circe.parser.decode
import io.github.mvillafuertem.chat.domain.model.{ Jwt, User }
import japgolly.scalajs.react.component.Js
import japgolly.scalajs.react.component.Js.{ RawMounted, UnmountedWithRawType }
import japgolly.scalajs.react.component.JsFn.Unmounted
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.VdomNode
import japgolly.scalajs.react.{ AsyncCallback, Children, CtorType, JsComponent, ScalaFnComponent }
import sttp.client3.circe.asJson
import sttp.client3.{ basicRequest, FetchBackend, Identity, RequestT, Response, ResponseException, SttpBackend, UriContext }
import sttp.model.{ MediaType, StatusCode }
import typings.react.mod._
import typings.std.global.{ console, localStorage }
import typings.sweetalert2.mod.{ default => Swal, SweetAlertIcon }

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

    val login: js.Function2[String, String, Unit] = (email: String, password: String) => {

      val url = "http://localhost:8080/api/v1/auth/login"

      val requestPOST = basicRequest
        .post(uri"$url")
        .contentType(MediaType.ApplicationJson)
        .response(asJson[Jwt])
        .body(s"""{"email":"$email","name": "$email","password": "$password"}""")

      def result: Future[Either[ResponseException[String, circe.Error], Jwt]] =
        requestPOST.send(sttpBackend).map(_.body)

      AsyncCallback
        .fromFuture(result.transformWith {
          case Failure(exception) =>
            console.log("Exception")
            console.log(exception)
            Swal.fire("Error", "Verifique el email o contraseña", SweetAlertIcon.error).toFuture
          case Success(value) =>
            console.log("Success")
            console.log(value)
            value.fold(
              _ => Swal.fire("Error", "Verifique el email o contraseña", SweetAlertIcon.error).toFuture,
              jwt =>
                Future.successful {
                  localStorage.setItem("token", jwt.token)
                  setAuth(Some(User(email, email, "", Some(true), Some("123"))))
                }
            )

        })
        .runNow()

    }

    val register: js.Function3[String, String, String, Unit] = (name, email, password) => {

      val url = "http://localhost:8080/api/v1/auth/login/new"

      val requestPOST: RequestT[Identity, Either[ResponseException[String, circe.Error], User], Any] = basicRequest
        .post(uri"$url")
        .contentType(MediaType.ApplicationJson)
        .response(asJson[User])
        .body(s"""{"email":"$email","name": "$name","password": "$password"}""")

      def result: Future[Response[Either[ResponseException[String, circe.Error], User]]] = requestPOST.send(sttpBackend)

      AsyncCallback
        .fromFuture(result.transformWith {
          case Failure(exception) =>
            console.log(exception)
            Swal.fire("Error", "El usuario ya existe", SweetAlertIcon.error).toFuture
          case Success(value) =>
            console.log(value)
            Future {
              value.code match {
                case StatusCode.Ok =>
                  value.body.fold(
                    _ => setAuth(None),
                    user =>
                      login(user.email, user.password)
                      // setAuth(Some(user))
                  )
                case StatusCode.Unauthorized => setAuth(None)
              }
            }
        })
        .runNow()
    }

    val verifyToken: js.Function1[Unit, Unit] =
      useCallback[js.Function1[Unit, Unit]](
        _ =>
          Option(localStorage.getItem("token"))
            .fold(setAuth(None)) { token =>
              val url = "http://localhost:8080/api/v1/auth/login/renew"

              val requestGET = basicRequest
                .get(uri"$url")
                .header("X-Auth-Token", token)
                .contentType(MediaType.ApplicationJson)

              def result: Future[Response[Either[String, String]]] = requestGET.send(sttpBackend)

              AsyncCallback
                .fromFuture(result.transformWith {
                  case Failure(exception) =>
                    console.log(exception)
                    Future(setAuth(None))
                  case Success(value) =>
                    console.log("Success")
                    Future {
                      value.code match {
                        case StatusCode.Ok =>
                          for {
                            body <- value.body
                            jwt  <- decode[Jwt](body)
                            _ = localStorage.setItem("token", jwt.token)
                          } yield ()
                          setAuth(Some(User("email", "email", "", Some(true), Some("123"))))
                        case StatusCode.Unauthorized => setAuth(None)
                      }
                    }

                })
                .runNow()
            },
        js.Array[js.Any]()
      )

    val logout: js.Function0[Unit] = () => {
      localStorage.removeItem("token")
      setAuth(None)
    }

    val ProviderComponent: Js.Component[ProviderProps[ChatState], Null, CtorType.Props] =
      JsComponent[ProviderProps[ChatState], Children.None, Null](AuthContext.value.Provider)

    val value: UnmountedWithRawType[ProviderProps[ChatState], Null, RawMounted[ProviderProps[ChatState], Null]] = ProviderComponent(
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
  // AuthContext.value.setProvider(value)
  }

}
