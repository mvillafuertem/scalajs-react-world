package io.github.mvillafuertem

import io.github.mvillafuertem.JournalApp.Main.ConnectedDemo
import io.github.mvillafuertem.auth.LoginScreen
import io.github.mvillafuertem.reducers.{AuthAction, AuthState}
import io.github.mvillafuertem.store.Store
import japgolly.scalajs.react.CtorType
import japgolly.scalajs.react.component.Js.Component
import japgolly.scalajs.react.component.ScalaFn
import org.scalajs.dom.document
import typings.reactRedux.components.Provider
import zio.{App, ExitCode, IO, ZIO}

import scala.scalajs.js
import scala.scalajs.js.Dictionary

object JournalApp extends App {

  object Main {


    //val components: ScalaFn.Component[Dictionary[ReduxFacade.Connected[AuthState, AuthAction]] with LoginScreen.Props, CtorType.Props] = LoginScreen.component

    val ConnectedDemo: Component[LoginScreen.Props, Null, CtorType.PropsAndChildren] =
      ReduxFacade.simpleConnect(Store, LoginScreen.component)

  }

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    IO.effectTotal {
      Provider(Store)(
        ConnectedDemo({
          val props = (new js.Object).asInstanceOf[LoginScreen.Props]
          props
        })()
      ).renderIntoDOM(document.getElementById("container"))
    }.exitCode



}
