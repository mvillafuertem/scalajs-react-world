package io.github.mvillafuertem

import io.github.mvillafuertem.AuthProvider.AuthComponentProps
import io.github.mvillafuertem.GraphService.getUserWeekCalendar
import japgolly.scalajs.react.component.Scala.BackendScope
import japgolly.scalajs.react.{ Callback, ScalaComponent }
import typings.microsoftGraph.microsoftGraphStrings.undefined
import typings.microsoftGraph.mod.Event
import typings.moment.momentMod
import typings.moment.momentStrings.week
import typings.momentTimezone.{ mod => moment }
import typings.windowsIana.mod.findOneIana
import japgolly.scalajs.react.vdom.html_<^._

import scala.scalajs.js
import scala.scalajs.js.|

object Calendar {

  case class CalendarState(eventsLoaded: Boolean = false, events: Array[Event] = Array(), startOfWeek: momentMod.Moment | undefined = undefined)

  final class Backend($ : BackendScope[AuthComponentProps, CalendarState]) {

    def render(props: AuthComponentProps, state: CalendarState) = <.div()

    def componentDidMount: Callback =
      (for {
        eventsLoaded <- $.state.map(_.eventsLoaded).asAsyncCallback
        _ <- (for {
          props <- $.props.asAsyncCallback
          user = props.user
          // Get the user's access token
          accessToken <- props.getAccessToken(js.Array("user.read", "mailboxsettings.read", "calendars.readwrite"))

          // Convert user's Windows time zone ("Pacific Standard Time")
          // to IANA format ("America/Los_Angeles")
          // Moment needs IANA format
          ianaTimeZone = findOneIana(user.timeZone)

          // Get midnight on the start of the current week in the user's timezone,
          // but in UTC. For example, for Pacific Standard Time, the time value would be
          // 07:00:00Z
          startOfWeek: momentMod.Moment = moment.tz(ianaTimeZone.get.toString).startOf(week).utc()

          // Get the user's events
          // events: Array[Event] <- getUserWeekCalendar("accessToken", "user.timeZone", startOfWeek).toCallback

          _ <- getUserWeekCalendar("accessToken", "user.timeZone", startOfWeek).flatMapSync(events => $.setState(CalendarState(true, events, startOfWeek)))
          // Update the array of events in state
          // _ <- .asAsyncCallback
        } yield ()).when(!eventsLoaded)

      } yield ()).handleErrorSync(e => $.props.flatMap(_.setError("ERROR", js.JSON.stringify(e.getMessage)))).toCallback

  }

  ScalaComponent
    .builder[AuthComponentProps]
    .initialState[CalendarState](CalendarState())
    .renderBackend[Backend]
    .componentDidMount(_.backend.componentDidMount)
    .build
}
