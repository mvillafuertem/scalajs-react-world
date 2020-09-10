package io.github.mvillafuertem

package object routers {

  /* the production build is deployed at github pages under /heroes , while dev build is server from root of webpack-dev-server */
  val basename: String = if (scala.scalajs.runtime.linkingInfo.productionMode) "/zio-react/heroes/" else ""

}
