package io.github.mvillafuertem

import japgolly.scalajs.react.component.Scala.BackendScope
import japgolly.scalajs.react.{ AsyncCallback, ScalaComponent }
import org.scalablytyped.runtime.StringDictionary
import typings.azureMsalBrowser.configurationMod.{ BrowserAuthOptions, CacheOptions, Configuration }
import typings.azureMsalBrowser.publicClientApplicationMod.PublicClientApplication
import typings.azureMsalBrowser.silentRequestMod.SilentRequest
import typings.azureMsalCommon.authorizationUrlRequestMod.AuthorizationUrlRequest

import scala.scalajs.js
import scala.scalajs.js.|

object AuthProvider {

  case class AuthProviderState(error: js.Any = js.Object, isAuthenticated: Boolean = false, user: js.Any = js.Object)

  class Backend($ : BackendScope[Unit, AuthProviderState]) {

    private val configuration: Configuration = Configuration()
      .setAuth(
        BrowserAuthOptions("appId")
          .setRedirectUri("")
      )
      .setCache(
        CacheOptions()
          .setCacheLocation("sessionStorage")
          .setStoreAuthStateInCookie(true)
      )

    val publicClientApplication = new PublicClientApplication(configuration)

    def render(s: AuthProviderState) = ???

    def componentDidMount() {

      val accounts = this.publicClientApplication.getAllAccounts()

      if (accounts.length > 0) {
        // Enhance user object with data from Graph
        this.getUserProfile()
      }
    }

    def login() =
      // Login via popup
      (for {
        _           <- AsyncCallback.fromJsPromise(this.publicClientApplication.loginPopup(AuthorizationUrlRequest(js.Array()).setPrompt("select_account")))
        userProfile <- this.getUserProfile()
      } yield userProfile)
        .handleErrorSync(e => $.modState(_.copy(isAuthenticated = false, user = null, error = normalizeError(e))))

    def logout() =
      AsyncCallback.fromJsPromise(this.publicClientApplication.logout())

    def getUserProfile(): AsyncCallback[Unit] =
      (for {
        accessToken <- this.getAccessToken(js.Array()) // TODO config.scopes
        user        <- GraphService.getUserDetails(accessToken)
        _           <- $.modState(_.copy(isAuthenticated = true, error = StringDictionary("message" -> "Access token:", "debug" -> accessToken))).asAsyncCallback
      } yield ())
        .handleError(e => $.modState(_.copy(isAuthenticated = true, user = null, error = e.getMessage)).async)

    def getAccessToken(scopes: js.Array[String]): AsyncCallback[String] = {
      val accounts = this.publicClientApplication.getAllAccounts()

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

    def normalizeError(error: String | Throwable) =
      (error: Any) match { // (the upcast to Any silences a warning for Scala 2’s type system). https://contributors.scala-lang.org/t/union-types-in-scala-3/4046/7
        case e: String if e.split('|').length > 1 =>
          StringDictionary("message" -> e.split('|')(1), "debug" -> e.split('|')(0))
        case e: String                            =>
          StringDictionary("message" -> e)
        case e: Throwable                         =>
          StringDictionary("message" -> e.getMessage, "debug" -> js.JSON.stringify(e.getMessage))
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

}
