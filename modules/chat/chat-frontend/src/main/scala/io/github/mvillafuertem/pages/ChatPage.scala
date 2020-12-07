package io.github.mvillafuertem.pages

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^.{ <, _ }
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }

object ChatPage {

  val component: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { _ =>
    <.div(^.className := "messaging")(
      <.div(^.className := "inbox_msg")(
        <.div(^.className := "inbox_people")(
          <.div(^.className := "headind_srch")(
            <.div(^.className := "recent_heading mt-2")(<.h4("Reciente")),
            <.div(^.className := "srch_bar")(<.div(^.className := "stylish-input-group")(<.button(^.className := "btn text-danger")("Salir"))),
            <.div(^.className := "inbox_chat")(
              <.div(^.className := "chat_list active_chat")(
                <.div(^.className := "chat_people")(
                  <.div(^.className := "chat_img")(
                    <.img(^.src     := "https://ptetutorials.com/images/user-profile.png", ^.alt := "sunil")
                  )
                ),
                <.div(^.className := "chat_ib")(
                  <.h5("Some random name"),
                  <.span(^.className := "text-success")("Online"),
                  <.span(^.className := "text-danger")("Offline")
                )
              ),
              <.div(^.className := "chat_list")(
                <.div(^.className := "chat_people")(
                  <.div(^.className := "chat_img")(
                    <.img(^.src     := "https://ptetutorials.com/images/user-profile.png", ^.alt := "sunil")
                  ),
                  <.div(^.className := "chat_ib")(
                    <.h5("Sunil Rajput")(
                      <.span(^.className := "chat_date")("Dec 2"),
                      <.p("Test, which is a new approach to have all solution astrology under one roof.")
                    )
                  )
                )
              ),
              <.div(^.className := "extra_space"),
              <.div(^.className := "mesgs")(
                <.div(^.className := "msg_history")(
                  <.div(^.className := "incoming_msg")(
                    <.div(^.className := "incoming_msg_img")(
                      <.img(^.src     := "https://ptetutorials.com/images/user-profile.png", ^.alt := "sunil")
                    ),
                    <.div(^.className := "received_msg")(
                      <.div(^.className := "received_withd_msg")(
                        <.p("Test which is a new approach to have all solutions"),
                        <.span(^.className := "time_date")("11:01 AM | June")
                      )
                    )
                  ),
                  <.div(^.className := "outgoing_msg")(
                    <.div(^.className := "sent_msg")(
                      <.p("Test which is a new approach to have al solutions"),
                      <.span(^.className := "time_date")("11:01 AM | June")
                    )
                  )
                ),
                <.div(^.className := "type_msg row")(
                  <.div(^.className := "input_msg_write col-sm-9")(<.input(^.`type` := "text", ^.className := "write_msg", ^.placeholder := "Mensaje...")),
                  <.div(^.className := "col-sm-3 text-center")(<.button(^.className := "msg_send_btn mt-3", ^.`type` := "button")("enviar"))
                )
              )
            )
          )
        )
      )
    )

  }

}
