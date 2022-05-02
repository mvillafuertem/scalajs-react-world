package io.github.mvillafuertem

import io.github.mvillafuertem.GraphService.Options.OptionsOps
import japgolly.scalajs.react.AsyncCallback
import org.scalablytyped.runtime.StringDictionary
import typings.microsoftGraph.mod.Event
import typings.microsoftMicrosoftGraphClient.clientMod.Client
import typings.microsoftMicrosoftGraphClient.iauthproviderMod.AuthProvider
import typings.microsoftMicrosoftGraphClient.iauthprovidercallbackMod.AuthProviderCallback
import typings.microsoftMicrosoftGraphClient.mod.PageIterator
import typings.microsoftMicrosoftGraphClient.pageIteratorMod.PageCollection
import typings.microsoftMicrosoftGraphClient.{ ioptionsMod, mod => graph }
import typings.moment.mod.Moment
import typings.moment.momentMod
import typings.moment.momentStrings.day
import typings.momentTimezone.{ mod => moment }

import scala.scalajs.js

object GraphService {

  val getAuthenticatedClient: js.Function1[String, Client] = (accessToken: String) =>
//    graph.Client.init(js.Dynamic.literal(
//      authProvider = (done: AuthProviderCallback) => done(null, accessToken)
//    ).asInstanceOf[Options])
    graph.Client.init(Options().setAuthProvider((done: AuthProviderCallback) => done(null, accessToken)))

  val getUserDetails: js.Function1[String, AsyncCallback[User]] = (accessToken: String) =>
    AsyncCallback.fromJsPromise {
      val client = getAuthenticatedClient(accessToken)
      val request = client
        .api("/me")
        .select(js.Array[String]("displayName", "mail", "userPrincipalName"))
      println(js.JSON.stringify(request))
      request.get()
    }.map { response =>
      val dynamic = response.asInstanceOf[js.Dynamic]
      User(dynamic.userPrincipalName.asInstanceOf[String], dynamic.displayName.asInstanceOf[String], dynamic.mail.asInstanceOf[String])
    }

  val getUserWeekCalendar: js.Function3[String, String, momentMod.Moment, AsyncCallback[Array[Event]]] =
    (accessToken: String, timeZone: String, startDate: momentMod.Moment) => {
      val client = getAuthenticatedClient(accessToken)
      for {
        response <- AsyncCallback.fromJsPromise {
          // Generate startDateTime and endDateTime query params
          // to display a 7-day window
          val startDateTime = startDate.format()
          val endDateTime   = moment(startDateTime).add(7, day).format()

          // GET /me/calendarview?startDateTime=''&endDateTime=''
          // &$select=subject,organizer,start,end
          // &$orderby=start/dateTime
          // &$top=50
          val request = client
            .api("/me/calendarview")
            .header("Prefer", s"outlook.timezone=${timeZone}")
            .query(StringDictionary(startDateTime -> startDateTime, endDateTime -> endDateTime))
            .select(js.Array[String]("subject", "organizer", "start", "end"))
            .orderby("start/dateTime")
            .top(50)

          println(js.JSON.stringify(request))
          request.get()
        }.map(_.asInstanceOf[PageCollection])
        events: Array[Event] = Array()

        result <- response.get("@odata.nextLink").fold(AsyncCallback.pure(response.value.asInstanceOf[Array[Event]])) { _ =>
          val iterator = new PageIterator(
            client.asInstanceOf[graph.Client],
            response,
            (event: Any) => {
              events.appended(event)
              true
            }
          )
          AsyncCallback.fromJsPromise(iterator.iterate()).map(_ => events)
        }

      } yield result
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
