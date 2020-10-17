package io.github.mvillafuertem

import japgolly.scalajs.react.AsyncCallback
import typings.microsoftMicrosoftGraphClient.ioptionsMod.Options
import typings.microsoftMicrosoftGraphClient.{ clientMod => graph }

import scala.scalajs.js

object GraphService {

  val getAuthenticatedClient: js.Function1[String, graph.Client] = (accessToken: String) => {
    graph.Client.init(new Options {
      authProvider { (done, _) =>
        done.asInstanceOf[js.Dynamic](null, accessToken)
      }
    })
  }

  val getUserDetails: js.Function1[String, AsyncCallback[Any]] = (accessToken: String) =>
    AsyncCallback.fromJsPromise {

      val client = getAuthenticatedClient(accessToken)

      client
        .api("/me")
        .select(js.Array[String]("displayName", "mail", "mailboxSettings", "userPrincipalName"))
        .get()

    }

}
