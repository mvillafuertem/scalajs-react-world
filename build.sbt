import java.nio.file.Files
import java.nio.file.StandardCopyOption.REPLACE_EXISTING

import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport.useYarn

lazy val `scalajs-react-world` = (project in file("."))
  .aggregate(`gif-finder`)
  .aggregate(`simple-test`)
  .aggregate(calendar)
  .aggregate(dashboard)
  .aggregate(heroes)
  .aggregate(journal)

lazy val calendar =
  (project in file("modules/calendar"))
    .enablePlugins(ScalablyTypedConverterPlugin)
    .enablePlugins(ScalaJSPlugin)
    .configure(baseSettings, browserProject, reactNpmDeps, bundlerSettings, withCssLoading)
    .settings(
      addCommandAlias("calendar", "project calendar;fastOptJS::startWebpackDevServer;~fastOptJS"),
      webpackDevServerPort := 8008,
      stFlavour            := Flavour.Japgolly,
      libraryDependencies ++= Seq("com.github.japgolly.scalacss" %%% "ext-react" % "0.6.1"),
      stIgnore ++= List("bootstrap", "@fortawesome/fontawesome-free"),
      Compile / npmDependencies ++= Seq(
        "@azure/msal-browser"               -> "2.3.0",
        "@fortawesome/fontawesome-free"     -> "5.14.0",
        "@microsoft/microsoft-graph-client" -> "2.1.0",
        "@types/react-router-dom"           -> "5.1.2",
        "@types/reactstrap"                 -> "8.5.1",
        "bootstrap"                         -> "4.5.2",
        "react-router-dom"                  -> "5.1.2",
        "reactstrap"                        -> "8.5.1"
      )
    )

lazy val `gif-finder` =
  (project in file("modules/gif-finder"))
    .enablePlugins(ScalablyTypedConverterPlugin)
    .enablePlugins(ScalaJSPlugin)
    .configure(baseSettings, browserProject, reactNpmDeps, bundlerSettings, withCssLoading)
    .settings(
      addCommandAlias("gif-finder", "project gif-finder;fastOptJS::startWebpackDevServer;~fastOptJS"),
      webpackDevServerPort := 8008,
      stFlavour            := Flavour.Slinky,
      libraryDependencies ++= Seq("me.shadaj" %%% "slinky-hot" % "0.6.6")
    )

lazy val heroes =
  (project in file("modules/heroes"))
    .enablePlugins(ScalablyTypedConverterPlugin)
    .enablePlugins(ScalaJSPlugin)
    .configure(baseSettings, browserProject, reactNpmDeps, bundlerSettings, withCssLoading)
    .settings(
      addCommandAlias("heroes", "project heroes;fastOptJS::startWebpackDevServer;~fastOptJS"),
      webpackDevServerPort := 8008,
      stFlavour            := Flavour.Slinky,
      libraryDependencies ++= Seq("me.shadaj" %%% "slinky-hot" % "0.6.6"),
      Compile / npmDependencies ++= Seq(
        "react-router-dom"        -> "5.1.2",
        "@types/react-router-dom" -> "5.1.2",
        "query-string"            -> "6.13.1"
      )
    )

lazy val journal =
  (project in file("modules/journal"))
    .enablePlugins(ScalablyTypedConverterPlugin)
    .enablePlugins(ScalaJSPlugin)
    .configure(baseSettings, browserProject, reactNpmDeps, bundlerSettings, withCssLoading)
    .settings(
      addCommandAlias("journal", "project journal;fastOptJS::startWebpackDevServer;~fastOptJS"),
      webpackDevServerPort := 8008,
      stFlavour            := Flavour.Japgolly,
      libraryDependencies ++= Seq("com.github.japgolly.scalacss" %%% "ext-react" % "0.6.1"),
      Compile / npmDependencies ++= Seq(
        "react-router-dom"         -> "5.1.2",
        "@types/react-router-dom"  -> "5.1.2",
        "query-string"             -> "6.13.1",
        "react-redux"              -> "7.2.1",
        "@types/react-redux"       -> "7.1.9",
        "redux-devtools-extension" -> "2.13.8",
        "redux"                    -> "4.0.5",
        "firebase"                 -> "7.21.1",
        "redux-thunk"              -> "2.3.0",
        "validator"                -> "13.1.0",
        "@types/validator"         -> "13.1.0",
        "sweetalert2"              -> "10.3.5"
      )
    )

lazy val dashboard =
  (project in file("modules/dashboard"))
    .enablePlugins(ScalablyTypedConverterPlugin)
    .enablePlugins(ScalaJSPlugin)
    .configure(baseSettings, browserProject, reactNpmDeps, bundlerSettings)
    .settings(
      addCommandAlias("dashboard", "project dashboard;fastOptJS::startWebpackDevServer;~fastOptJS"),
      useYarn              := true,
      webpackDevServerPort := 8008,
      stFlavour            := Flavour.Slinky,
      libraryDependencies ++= Seq("me.shadaj" %%% "slinky-hot" % "0.6.6"),
      Compile / npmDependencies ++= Seq(
        "@material-ui/core"       -> "3.9.4", // note: version 4 is not supported yet
        "@material-ui/styles"     -> "3.0.0-alpha.10", // note: version 4 is not supported yet
        "@material-ui/icons"      -> "3.0.2",
        "recharts"                -> "1.8.5",
        "@types/recharts"         -> "1.8.10",
        "@types/classnames"       -> "2.2.10",
        "react-router-dom"        -> "5.1.2",
        "@types/react-router-dom" -> "5.1.2"
      )
    )

lazy val `simple-test` =
  (project in file("modules/simple-test"))
    .enablePlugins(ScalablyTypedConverterPlugin)
    .enablePlugins(ScalaJSPlugin)
    .configure(testConfiguration, reactNpmDeps)
    .settings(
      addCommandAlias("dev", ";fastOptJS::startWebpackDevServer;~fastOptJS"),
      addCommandAlias("build", "fullOptJS::webpack"),
      scalaVersion := "2.13.3",
      useYarn      := true,
      Compile / npmDependencies ++= Seq(
        "@material-ui/core"       -> "3.9.4", // note: version 4 is not supported yet
        "@material-ui/styles"     -> "3.0.0-alpha.10", // note: version 4 is not supported yet
        "@material-ui/icons"      -> "3.0.2",
        "@types/classnames"       -> "2.2.10",
        "react-router-dom"        -> "5.1.2",
        "@types/react-router-dom" -> "5.1.2", // note 5.1.4 did weird things to the Link component
        "react-proxy"             -> "1.1.8",
        "recharts"                -> "1.8.5",
        "@types/recharts"         -> "1.8.10"
      ),
      Compile / npmDevDependencies ++= Seq(
        "file-loader"         -> "6.0.0",
        "style-loader"        -> "1.2.1",
        "css-loader"          -> "3.5.3",
        "html-webpack-plugin" -> "4.3.0",
        "copy-webpack-plugin" -> "5.1.1",
        "webpack-merge"       -> "4.2.2"
      ),
      libraryDependencies ++= Seq(
        "me.shadaj"     %%% "slinky-hot" % "0.6.6",
        "org.scalatest" %%% "scalatest"  % "3.2.2" % Test
      ),
      stFlavour                             := Flavour.Slinky,
      fastOptJS / webpackBundlingMode       := BundlingMode.LibraryOnly(),
      fastOptJS / webpackConfigFile         := Some(baseDirectory.value / "webpack" / "webpack-fastopt.config.js"),
      fastOptJS / webpackDevServerExtraArgs := Seq("--inline", "--hot"),
      fullOptJS / webpackConfigFile         := Some(baseDirectory.value / "webpack" / "webpack-opt.config.js"),
      scalacOptions += "-Ymacro-annotations",
      startWebpackDevServer / version := "3.11.0",
      webpack / version               := "4.43.0",
      webpackResources                := baseDirectory.value / "webpack" * "*"
    )

lazy val testConfiguration: Project => Project =
  _.settings(
    Test / requireJsDomEnv   := true,
    Test / webpackConfigFile := Some(baseDirectory.value / "webpack" / "webpack-core.config.js"),
    stIgnore := List(
      "react-proxy",
      "jss-plugin-props-sort",
      "jss-props-sort",
      "jss",
      "jss-vendor-prefixer",
      "jss-default-unit",
      "jss-camel-case",
      "jss-nested",
      "jss-global",
      "jss-plugin-nested"
    ),
    Compile / npmDependencies ++= Seq(
      "jss-props-sort"        -> "6.0.0",
      "jss-vendor-prefixer"   -> "8.0.1",
      "jss-default-unit"      -> "8.0.2",
      "jss-camel-case"        -> "6.1.0",
      "jss-nested"            -> "6.0.1",
      "jss-global"            -> "3.0.0",
      "jss-plugin-global"     -> "10.4.0",
      "jss-plugin-nested"     -> "10.4.0",
      "jss-plugin-props-sort" -> "10.4.0",
      "jss"                   -> "10.4.0"
    )
  )

lazy val withCssLoading: Project => Project =
  _.settings(
    /* custom webpack file to include css */
    webpackConfigFile := Some((ThisBuild / baseDirectory).value / "webpack" / "custom.webpack.config.js"),
    Compile / npmDevDependencies ++= Seq(
      "webpack-merge" -> "4.2.2",
      "css-loader"    -> "3.4.2",
      "sass-loader"   -> "10.0.2",
      "sass"          -> "1.26.11",
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
      useYarn      := true,
      scalaVersion := "2.13.3",
      scalacOptions ++= ScalacOptions.flags,
      scalacOptions += "-Ymacro-annotations",
      scalaJSUseMainModuleInitializer := true,
      scalaJSLinkerConfig ~= (/* disabled because it somehow triggers many warnings */
      _.withSourceMap(false)
        .withModuleKind(ModuleKind.CommonJSModule)),
      libraryDependencies ++= Seq(
        "dev.zio"                      %%% "zio"             % "1.0.1",
        "io.github.cquiroz"            %%% "scala-java-time" % "2.0.0",
        "org.scalatest"                %%% "scalatest"       % "3.2.2" % Test,
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
