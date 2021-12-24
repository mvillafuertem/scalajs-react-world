package io.github.mvillafuertem

import io.github.mvillafuertem.ErrorMessage.ErrorMessageProps
import japgolly.scalajs.react.component.Scala.BackendScope
import japgolly.scalajs.react.component.ScalaFn
import japgolly.scalajs.react.{ AsyncCallback, Callback, CtorType, ScalaComponent }
import typings.azureMsalBrowser.configurationMod.{ BrowserAuthOptions, CacheOptions, Configuration }
import typings.azureMsalBrowser.mod.PublicClientApplication
import typings.azureMsalBrowser.silentRequestMod.SilentRequest
import typings.azureMsalCommon.authorizationUrlRequestMod.AuthorizationUrlRequest

import scala.language.existentials
import scala.scalajs.js
import scala.scalajs.js.|

object AuthProvider {

  case class AuthComponentProps(
    error:           ErrorMessageProps,
    isAuthenticated: Boolean,
    user:            User,
    login:           js.Function0[Callback],
    logout:          js.Function0[Callback],
    getAccessToken:  js.Function1[js.Array[String], AsyncCallback[String]],
    setError:        js.Function2[String, String, Callback]
  )

  case class AuthProviderState(error: ErrorMessageProps = ErrorMessageProps(), isAuthenticated: Boolean = false, user: User = User())

  def withAuthProvider(WrappedComponent: CtorType.Props[AuthComponentProps, ScalaFn.Unmounted[AuthComponentProps]]) = {
    class Backend($ : BackendScope[Unit, AuthProviderState]) {

      private val configuration: Configuration = Configuration()
        .setAuth(
          BrowserAuthOptions("")
            .setRedirectUri("http://localhost:8008")
        )
        .setCache(
          CacheOptions()
            .setCacheLocation("sessionStorage")
            .setStoreAuthStateInCookie(true)
        )

      val publicClientApplication = new PublicClientApplication(configuration)

      def render(state: AuthProviderState): ScalaFn.Unmounted[AuthComponentProps] =
        WrappedComponent(AuthComponentProps(state.error, state.isAuthenticated, state.user, login(), logout(), getAccessToken, setErrorMessage()))

      def componentDidMount: Callback =
        this.publicClientApplication
          .getAllAccounts()
          .headOption
          .fold(AsyncCallback.unit)(_ => this.getUserProfile)
          .toCallback

      def login(): js.Function0[Callback] = () =>
        // Login via popup
        (for {
          _ <- AsyncCallback.fromJsPromise(
            this.publicClientApplication.loginPopup(
              AuthorizationUrlRequest(js.Array("user.read", "mailboxsettings.read", "calendars.readwrite")).setPrompt("select_account")
            )
          )
          userProfile <- this.getUserProfile
        } yield userProfile)
          .handleErrorSync(e => $.modState(_.copy(isAuthenticated = false, user = null, error = normalizeError(e))))
          .toCallback

      def logout(): js.Function0[Callback] = () => AsyncCallback.fromJsPromise(this.publicClientApplication.logout()).toCallback

      def getUserProfile: AsyncCallback[Unit] =
        (for {
          accessToken <- this.getAccessToken(js.Array("user.read", "mailboxsettings.read", "calendars.readwrite"))
          user        <- GraphService.getUserDetails(accessToken)
          _ <- $.modState(
            _.copy(user = user, isAuthenticated = true, error = ErrorMessageProps(debug = accessToken, message = "Access token:"))
          ).asAsyncCallback
        } yield ())
          .handleError(e => $.modState(_.copy(isAuthenticated = false, user = null, error = ErrorMessageProps(message = e.getMessage))).async)

      def getAccessToken: js.Function1[js.Array[String], AsyncCallback[String]] = scopes => {
        val accounts = this.publicClientApplication.getAllAccounts()

        println(js.JSON.stringify(accounts))
        if (accounts.length <= 0) throw new Error("login_required")
        // Get the access token silently
        // If the cache contains a non-expired token, this function
        // will just return the cached token. Otherwise, it will
        // make a request to the Azure OAuth endpoint to get a token
        AsyncCallback
          .fromJsPromise(
            this.publicClientApplication
              .acquireTokenSilent(SilentRequest(accounts.head, scopes))
          )
          .handleError {
            case e if isInteractionRequired(e) => AsyncCallback.fromJsPromise(this.publicClientApplication.acquireTokenPopup(AuthorizationUrlRequest(scopes)))
          }
          .map(_.accessToken)
      }

      def setErrorMessage(): js.Function2[String, String, Callback] =
        (message, debug) =>
          $.setState(
            AuthProviderState(
              error = ErrorMessageProps(message, debug)
            )
          )

      def normalizeError(error: String | Throwable): ErrorMessageProps =
        (error: Any) match { // (the upcast to Any silences a warning for Scala 2’s type system). https://contributors.scala-lang.org/t/union-types-in-scala-3/4046/7
          case e: String if e.split('|').length > 1 =>
            ErrorMessageProps(debug = e.split('|')(0), message = e.split('|')(1))
          case e: String =>
            ErrorMessageProps(message = e)
          case e: Throwable =>
            println(e.getMessage)
            ErrorMessageProps(debug = js.JSON.stringify(e.getMessage), message = e.getMessage)
        }

      def isInteractionRequired(error: Throwable): Boolean =
        if (error.getMessage.nonEmpty) {
          false
        } else {
          error.getMessage.indexOf("consent_required") > -1 ||
          error.getMessage.indexOf("interaction_required") > -1 ||
          error.getMessage.indexOf("login_required") > -1 ||
          error.getMessage.indexOf("no_account_in_silent_request") > -1
        }
    }

    ScalaComponent
      .builder[Unit]
      .initialState[AuthProviderState](AuthProviderState())
      .renderBackend[Backend]
      .componentDidMount(_.backend.componentDidMount)
      .build

  }

}
