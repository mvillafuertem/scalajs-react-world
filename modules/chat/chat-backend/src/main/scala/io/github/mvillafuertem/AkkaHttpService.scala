//package io.github.mvillafuertem
//
//import akka.actor.ActorSystem
//import akka.http.scaladsl.Http
//import akka.http.scaladsl.model.ContentType
//import akka.http.scaladsl.model.HttpCharsets.`UTF-8`
//import akka.http.scaladsl.model.MediaTypes.`text/html`
//import akka.http.scaladsl.server.{ Directives, Route }
//import akka.stream.ActorMaterializer
//import com.typesafe.config.ConfigFactory
//
//final class AkkaHttpService extends Directives {
//
//  def assets: Route =
//    getFromResourceDirectory("assets") ~ pathSingleSlash {
//      get {
//        getFromResource("assets/index.html", ContentType(`text/html`, `UTF-8`))
//      }
//    }
//
//  val route = assets
//}
//
//object AkkaHttpService {
//
//  def main(args: Array[String]) {
//    implicit val system       = ActorSystem("server-system")
//    implicit val materializer = ActorMaterializer()
//
//    val config    = ConfigFactory.load()
//    val interface = config.getString("http.interface")
//    val port      = config.getInt("http.port")
//
//    val service = new AkkaHttpService()
//
//    Http().bindAndHandle(service.route, interface, port)
//
//    println(s"Server online at http://$interface:$port")
//  }
//
//}
