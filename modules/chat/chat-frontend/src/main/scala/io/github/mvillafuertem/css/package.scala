package io.github.mvillafuertem

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

package object css {

  @JSImport("resources/chat.css", JSImport.Namespace)
  @js.native
  object ChatCSS extends js.Object

  @JSImport("resources/login-register.css", JSImport.Namespace)
  @js.native
  object LoginRegisterCSS extends js.Object

  @JSImport("bootstrap/dist/css/bootstrap.min.css", JSImport.Namespace)
  @js.native
  object BootstrapMinCSS extends js.Object

  @JSImport("@fortawesome/fontawesome-free/css/all.css", JSImport.Namespace)
  @js.native
  object FontAwesomeCSS extends js.Object

}
