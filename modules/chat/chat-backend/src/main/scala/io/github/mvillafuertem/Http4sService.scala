//package io.github.mvillafuertem
//
//import cats.effect.{ Blocker, ExitCode, IO, IOApp, Resource }
//import org.http4s.{ Header, Headers, HttpRoutes, Method, Request, Response, StaticFile }
//import org.http4s.dsl.io.{ ->, GET, NotFound, Ok, Root }
//import org.http4s.server.Server
//import org.http4s.server.blaze.BlazeServerBuilder
//import org.http4s.server.middleware.CORS
//import cats.effect._
//import org.http4s.EntityDecoder._
//import org.http4s.EntityEncoder._
//import org.http4s.dsl.io._
//import org.http4s.server.Server
//import org.http4s.server.blaze.BlazeServerBuilder
//import org.http4s.server.middleware.CORS
//import org.http4s.syntax.kleisli._
//import org.http4s.{ Request, StaticFile, _ }
//
//import scala.concurrent.ExecutionContext.global
//
//object Http4sService extends IOApp {
//
//  override def run(args: List[String]): IO[ExitCode] =
//    IO(println("Server starting at Port 8080")) *>
//      app.use(_ => IO.never).as(ExitCode.Success)
//
//  private def static(file: String, blocker: Blocker, request: Request[IO]) =
//    StaticFile.fromResource("/assets/" + file, blocker, Some(request)).getOrElseF(NotFound())
//
//  private def routes(blocker: Blocker) = HttpRoutes
//    .of[IO] {
//      case req if req.method == Method.OPTIONS =>
//        IO(Response(Ok, headers = Headers.of(Header("Allow", "OPTIONS, POST"))))
//      case request @ GET -> Root =>
//        static("index.html", blocker, request)
//      case request @ GET -> path =>
//        static(path.toString, blocker, request)
//    }
//    .orNotFound
//
//  private val app: Resource[IO, Server[IO]] =
//    for {
//      blocker <- Blocker[IO]
//      server <- BlazeServerBuilder[IO](global)
//        .bindHttp(8080, "0.0.0.0")
//        .withHttpApp(CORS(routes(blocker)))
//        .resource
//    } yield server
//
//}
