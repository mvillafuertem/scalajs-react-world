import java.nio.file.Files
import java.nio.file.StandardCopyOption.REPLACE_EXISTING

lazy val `scalajs-react-world` = (project in file("."))
  .aggregate(heroes)
  .aggregate(`gif-finder`)

lazy val `gif-finder` =
  (project in file("modules/gif-finder"))
    .enablePlugins(ScalablyTypedConverterPlugin)
    .enablePlugins(ScalaJSPlugin)
    .configure(baseSettings, browserProject, reactNpmDeps, bundlerSettings, withCssLoading)
    .settings(
      addCommandAlias("gif-finder", "project gif-finder;fastOptJS::startWebpackDevServer;~fastOptJS"),
      webpackDevServerPort := 8008
    )

lazy val heroes =
  (project in file("modules/heroes"))
    .enablePlugins(ScalablyTypedConverterPlugin)
    .enablePlugins(ScalaJSPlugin)
    .configure(baseSettings, browserProject, reactNpmDeps, bundlerSettings, withCssLoading)
    .settings(
      addCommandAlias("heroes", "project heroes;fastOptJS::startWebpackDevServer;~fastOptJS"),
      webpackDevServerPort := 8008,
      Compile / npmDependencies ++= Seq(
        "react-router-dom"        -> "5.1.2",
        "@types/react-router-dom" -> "5.1.2",
        "query-string"            -> "6.13.1"
      )
    )

lazy val withCssLoading: Project => Project =
  _.settings(
    /* custom webpack file to include css */
    webpackConfigFile := Some((ThisBuild / baseDirectory).value / "custom.webpack.config.js"),
    Compile / npmDevDependencies ++= Seq(
      "webpack-merge" -> "4.2.2",
      "css-loader"    -> "3.4.2",
      "style-loader"  -> "1.1.3",
      "file-loader"   -> "5.1.0",
      "url-loader"    -> "3.0.0"
    )
  )

lazy val reactNpmDeps: Project => Project =
  _.settings(
    Compile / npmDependencies ++= Seq(
      "react"            -> "16.13.1",
      "react-dom"        -> "16.13.1",
      "@types/react"     -> "16.9.34",
      "@types/react-dom" -> "16.9.6"
    )
  )

/**
 * Custom task to start demo with webpack-dev-server, use as `<project>/start`.
 * Just `start` also works, and starts all frontend demos
 *
 * After that, the incantation is this to watch and compile on change:
 * `~<project>/fastOptJS::webpack`
 */
lazy val start = TaskKey[Unit]("start")

/**
 * Say just `dist` or `<project>/dist` to make a production bundle in
 * `docs` for github publishing
 */
lazy val dist = TaskKey[File]("dist")

lazy val baseSettings: Project => Project =
  _.enablePlugins(ScalaJSPlugin)
    .settings(
      useYarn := true,
      stFlavour := Flavour.Slinky,
      scalaVersion := "2.13.3",
      scalacOptions ++= ScalacOptions.flags,
      scalacOptions += "-Ymacro-annotations",
      scalaJSUseMainModuleInitializer := true,
      scalaJSLinkerConfig ~= (/* disabled because it somehow triggers many warnings */
      _.withSourceMap(false)
        .withModuleKind(ModuleKind.CommonJSModule)),
      /* for slinky */
      libraryDependencies ++= Seq("me.shadaj" %%% "slinky-hot" % "0.6.6"),
      libraryDependencies ++= Seq(
        "dev.zio"                      %%% "zio"             % "1.0.1",
        "io.github.cquiroz"            %%% "scala-java-time" % "2.0.0",
        "org.scalatest"                %%% "scalatest"       % "3.2.1" % Test,
        "com.softwaremill.sttp.client" %%% "core"            % "2.2.7",
        "com.softwaremill.sttp.client" %%% "circe"           % "2.2.7",
        "io.circe"                     %%% "circe-optics"    % "0.13.0",
        "io.circe"                     %%% "circe-generic"   % "0.13.0"
      )
    )

lazy val bundlerSettings: Project => Project =
  _.settings(
    Compile / fastOptJS / webpackExtraArgs += "--mode=development",
    Compile / fullOptJS / webpackExtraArgs += "--mode=production",
    Compile / fastOptJS / webpackDevServerExtraArgs += "--mode=development",
    Compile / fullOptJS / webpackDevServerExtraArgs += "--mode=production"
  )

/**
 * Implement the `start` and `dist` tasks defined above.
 * Most of this is really just to copy the index.html file around.
 */
lazy val browserProject: Project => Project =
  _.settings(
    start := {
      (Compile / fastOptJS / startWebpackDevServer).value
    },
    dist := {
      val artifacts      = (Compile / fullOptJS / webpack).value
      val artifactFolder = (Compile / fullOptJS / crossTarget).value
      val distFolder     = (ThisBuild / baseDirectory).value / "docs" / moduleName.value

      distFolder.mkdirs()
      artifacts.foreach { artifact =>
        val target = artifact.data.relativeTo(artifactFolder) match {
          case None          => distFolder / artifact.data.name
          case Some(relFile) => distFolder / relFile.toString
        }

        Files.copy(artifact.data.toPath, target.toPath, REPLACE_EXISTING)
      }

      val indexFrom = baseDirectory.value / "src/main/js/index.html"
      val indexTo   = distFolder / "index.html"

      val indexPatchedContent = {
        import collection.JavaConverters._
        Files
          .readAllLines(indexFrom.toPath, IO.utf8)
          .asScala
          .map(_.replaceAllLiterally("-fastopt-", "-opt-"))
          .mkString("\n")
      }

      Files.write(indexTo.toPath, indexPatchedContent.getBytes(IO.utf8))
      distFolder
    }
  )
