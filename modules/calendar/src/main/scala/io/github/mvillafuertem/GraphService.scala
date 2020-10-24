package io.github.mvillafuertem

import io.github.mvillafuertem.GraphService.Options.OptionsOps
import japgolly.scalajs.react.AsyncCallback
import typings.microsoftMicrosoftGraphClient.clientMod.Client
import typings.microsoftMicrosoftGraphClient.iauthproviderMod.AuthProvider
import typings.microsoftMicrosoftGraphClient.iauthprovidercallbackMod.AuthProviderCallback
import typings.microsoftMicrosoftGraphClient.iclientoptionsMod.ClientOptions
import typings.microsoftMicrosoftGraphClient.ioptionsMod
import typings.microsoftMicrosoftGraphClient.ioptionsMod.Options
import typings.microsoftMicrosoftGraphClient.{mod => graph}

import scala.scalajs.js

object GraphService {

  val getAuthenticatedClient: js.Function1[String, Client] = (accessToken: String) => {

//    graph.Client.init(js.Dynamic.literal(
//      authProvider = (done: AuthProviderCallback) => done(null, accessToken)
//    ).asInstanceOf[Options])
    graph.Client.init(Options().setAuthProvider((done: AuthProviderCallback) => done(null, accessToken)))
  }

  val getUserDetails: js.Function1[String, AsyncCallback[User]] = (accessToken: String) =>
    AsyncCallback.fromJsPromise {

      val client = getAuthenticatedClient(accessToken)

      val request = client
        .api("/me")
        .select(js.Array[String]("displayName", "mail", "userPrincipalName"))
      println(js.JSON.stringify(request))
      request.get()

    }.map{response =>
      val dynamic = response.asInstanceOf[js.Dynamic]
      User(dynamic.userPrincipalName.asInstanceOf[String],
        dynamic.displayName.asInstanceOf[String],
        dynamic.mail.asInstanceOf[String])
    }

  object Options {
    @scala.inline
    def apply(): ioptionsMod.Options = {
      val __obj = js.Dynamic.literal()
      __obj.asInstanceOf[ioptionsMod.Options]
    }

    @scala.inline
    implicit class OptionsOps[Self <: ioptionsMod.Options](val x: Self) extends AnyVal {
      @scala.inline
      def set(key: String, value: js.Any): Self = {
        x.asInstanceOf[js.Dynamic].updateDynamic(key)(value)
        x
      }
      @scala.inline
      def setAuthProvider(done: AuthProvider): Self = this.set("authProvider", done.asInstanceOf[js.Any])
    }

  }

}
