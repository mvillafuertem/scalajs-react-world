package io.github.mvillafuertem

import io.github.mvillafuertem.model.User
import slinky.core.facade.{React, ReactContext, SetStateHookCallback}

package object auth {

  val initialUser: User = User()

  val authContext: ReactContext[(User, AuthAction => Unit)] =
    React.createContext[(User, AuthAction => Unit)]((initialUser, {
      case AuthAction.Login(user) =>
      case AuthAction.Logout(user) =>
    }))

}
