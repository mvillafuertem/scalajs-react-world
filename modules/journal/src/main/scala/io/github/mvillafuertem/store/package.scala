package io.github.mvillafuertem

import io.github.mvillafuertem.auth.LoginScreen
import io.github.mvillafuertem.reducers._
import japgolly.scalajs.react.component.Js
import japgolly.scalajs.react.{Children, CtorType, JsComponent}
import typings.reactRedux.mod.connect
import typings.redux.mod.{applyMiddleware, _}
import typings.reduxDevtoolsExtension.mod.{EnhancerOptions, devToolsEnhancer}
import typings.reduxThunk.mod.default
import typings.std.ReturnType

import scala.scalajs.js
import scala.scalajs.js.|

package object store {

  lazy val reducers = combineReducers(
    js.Dynamic.literal(
      authReducer = AuthReducer.Reducer,
      uiReducer = UiReducer.Reducer
    )
  )

  applyMiddleware(default)

//  val StoreMiddleware =
//    createStore(
//      AuthReducer.Reducer,
//      composeWithDevTools(
//        applyMiddleware(default)
//      )
//    )

  type AppState = ReturnType[reducers.type]


  type AppActions = AuthAction | UiAction

  val mapStateToProps: js.Function1[AppState, js.Dynamic] =
    (state: AppState) => {
      println("MYSTATE ~ " + js.JSON.stringify(state))
      js.Dynamic.literal(state = state.asInstanceOf[js.Dynamic].authReducer)
    }

  val mapDispatchToProps: js.Function1[Dispatch[AppActions], js.Dynamic] =
    (dispatch: Dispatch[AppActions]) =>
      js.Dynamic.literal(dispatch = dispatch
      )

  val connectElem: Js.Component[LoginScreen.Props, Null, CtorType.PropsAndChildren] =
    JsComponent[LoginScreen.Props, Children.Varargs, Null](
      connect.asInstanceOf[js.Dynamic](mapStateToProps, mapDispatchToProps)(LoginScreen.component.toJsComponent.raw)
    )

  val Store =
    createStore(reducers, devToolsEnhancer(EnhancerOptions().setName("Journal Store")))

}
