import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

object WebpackSettings {

  lazy val value: Project => Project =
    _.settings(
      Test / webpackConfigFile              := Some(baseDirectory.value / "webpack" / "webpack-core.config.js"),
      fastOptJS / webpackBundlingMode       := BundlingMode.LibraryOnly(),
      fastOptJS / webpackConfigFile         := Some(baseDirectory.value / "webpack" / "webpack-fastopt.config.js"),
      fastOptJS / webpackDevServerExtraArgs := Seq("--inline", "--hot", "--disableHostCheck"),
      fullOptJS / webpackConfigFile         := Some(baseDirectory.value / "webpack" / "webpack-opt.config.js"),
//      Compile / fastOptJS / webpackExtraArgs += "--mode=development",
//      Compile / fullOptJS / webpackExtraArgs += "--mode=production",
//      Compile / fastOptJS / webpackDevServerExtraArgs += "--mode=development",
//      Compile / fullOptJS / webpackDevServerExtraArgs += "--mode=production",
      startWebpackDevServer / version := "3.11.0",
      webpack / version               := "4.43.0",
      webpackDevServerPort            := 8024,
      webpackResources                := baseDirectory.value / "webpack" * "*"
    )

}
