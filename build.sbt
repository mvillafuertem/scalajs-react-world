import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport.useYarn

import java.nio.file.Files
import java.nio.file.StandardCopyOption.REPLACE_EXISTING
import scala.sys.process.{ Process, _ }

lazy val `scalajs-react-world` = (project in file("."))
  .aggregate(calendar)
  .aggregate(`chat-backend`)
  .aggregate(`chat-frontend`)
  .aggregate(dashboard)
  .aggregate(`gif-finder`)
  .aggregate(`graph-viewer`)
  .aggregate(heroes)
  .aggregate(journal)
  .aggregate(laminar)
  .aggregate(`simple-test`)
  .settings(commands += Command.command("chat-release") { state =>
    "chat-frontend/clean" ::
      "chat-frontend/fullOptJS::webpack" ::
      "chat-backend/clean" ::
      "chat-backend/compile" ::
      "chat-backend/stage" ::
      state
  })

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
      Compile / npmDependencies ++= NpmDependencies.`calendar`
    )

lazy val `chat-backend` = (project in file("modules/chat/chat-backend"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(IntegrationTest / parallelExecution := false)
  .settings(scalaVersion := "2.13.6", organization := "io.github.mvillafuertem")
  .settings(testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")))
  .settings(
    Compile / unmanagedResourceDirectories += baseDirectory.value / "../chat-frontend/target/build",
    libraryDependencies ++= Seq(
      "dev.zio"           %% "zio-interop-reactivestreams" % "1.3.5",
      "dev.zio"           %% "zio-logging-slf4j"           % "0.5.11",
      "dev.zio"           %% "zio-streams"                 % "1.0.11",
      "dev.zio"           %% "zio"                         % "1.0.11",
      "com.typesafe.akka" %% "akka-http"                   % "10.2.6",
      "com.typesafe.akka" %% "akka-stream"                 % "2.6.16",
      "com.typesafe.akka" %% "akka-actor-typed"            % "2.6.16",
      "com.typesafe.akka" %% "akka-slf4j"                  % "2.6.16",
      "org.mongodb.scala" %% "mongo-scala-driver"          % "4.3.1",
      "ch.qos.logback"     % "logback-classic"             % "1.2.5",
      "org.http4s"        %% "http4s-dsl"                  % "0.23.1",
      "org.http4s"        %% "http4s-blaze-server"         % "0.23.1",
      "com.github.t3hnar" %% "scala-bcrypt"                % "4.3.0",
      "dev.zio"           %% "zio-test"                    % "1.0.11"  % IntegrationTest,
      "dev.zio"           %% "zio-test-sbt"                % "1.0.11"  % IntegrationTest,
      "org.scalatest"     %% "scalatest"                   % "3.2.9"  % IntegrationTest,
      "com.dimafeng"      %% "testcontainers-scala-core"   % "0.39.5" % IntegrationTest,
      "com.github.jwt-scala"     %% "jwt-circe"                   % "9.0.0"
    )
  )
  .configure(DockerSettings.value)
  .enablePlugins(JavaAppPackaging)
  .dependsOn(`chat-shared`.jvm)

lazy val `chat-frontend` = (project in file("modules/chat/chat-frontend"))
  .enablePlugins(ScalablyTypedConverterPlugin)
  .enablePlugins(ScalaJSPlugin)
  .configure(
    WebpackSettings.value,
    reactNpmDeps
  )
  .settings(
    addCommandAlias("chat-frontend", "project chat-frontend;set javaOptions  += \"-DIsLocal=true\";fastOptJS::startWebpackDevServer;~fastOptJS"),
    scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)),
    //scalaJSLinkerConfig ~= (_.withModuleSplitStyle(ModuleSplitStyle.SmallestModules)),
    stFlavour := Flavour.Japgolly,
    libraryDependencies ++= Seq("com.github.japgolly.scalacss" %%% "ext-react" % "0.6.1"),
    stIgnore ++= List("bootstrap", "@fortawesome/fontawesome-free"),
    Compile / npmDependencies ++= NpmDependencies.`chat-frontend`,
    Compile / npmDevDependencies ++= NpmDependencies.`chat-frontend-dev`
  )
  .settings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "1.1.0"
    )
  )
  .dependsOn(`chat-shared`.js)
  .settings(
    scalaVersion := "2.13.6",
    organization := "io.github.mvillafuertem",
    libraryDependencies ++= Seq(
      "dev.zio"           %%% "zio"             % "1.0.11",
      "io.github.cquiroz" %%% "scala-java-time" % "2.3.0",
      "org.scalatest"     %%% "scalatest"       % "3.2.9" % Test,
      "io.circe"          %%% "circe-optics"    % "0.14.1",
      "io.circe"          %%% "circe-generic"   % "0.14.1"
    ) ++ Seq(
      "com.softwaremill.sttp.client3" %%% "core",
      "com.softwaremill.sttp.client3" %%% "circe"
    ).map(_ % sttpVersion)
  )

lazy val tapirVersion = "0.17.9"
lazy val sttpVersion  = "3.2.0"

lazy val `chat-shared` = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/chat/chat-shared"))
  .settings(
    scalaVersion := "2.13.6",
    organization := "io.github.mvillafuertem"
  )
  .settings(
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-generic" % "0.14.1"
    ) ++ Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server",
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe",
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs",
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml",
      "com.softwaremill.sttp.tapir" %% "tapir-zio",
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-client"
    ).map(_ % tapirVersion)
  )

lazy val `countdown-native` =
  (project in file("modules/countdown-native"))
    .enablePlugins(ScalaJSPlugin)
    .enablePlugins(ScalablyTypedConverterExternalNpmPlugin)
    .settings(
      scalaVersion := "2.13.6",
      scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)),
      scalaJSUseMainModuleInitializer := false,
      /* ScalablyTypedConverterExternalNpmPlugin requires that we define how to install node dependencies and where they are */
      externalNpm := {
        Process("yarn", baseDirectory.value).!
        baseDirectory.value
      },
      stFlavour := Flavour.Japgolly,
      run := {
        (Compile / fastOptJS).value
        Process("expo start", baseDirectory.value).!
      }
    )

lazy val `counter-native` =
  (project in file("modules/counter-native"))
    .enablePlugins(ScalaJSPlugin)
    .enablePlugins(ScalablyTypedConverterExternalNpmPlugin)
    .settings(
      scalaVersion := "2.13.6",
      scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)),
      scalaJSUseMainModuleInitializer := false,
      externalNpm := {
        Process("yarn", baseDirectory.value).!
        baseDirectory.value
      },
      stFlavour := Flavour.Japgolly,
      run := {
        (Compile / fastOptJS).value
        Process("yarn react-native start", baseDirectory.value).!
      }
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
      libraryDependencies ++= Seq("me.shadaj" %%% "slinky-hot" % "0.6.7")
    )

lazy val `graph-viewer` =
  (project in file("modules/graph-viewer"))
    .enablePlugins(ScalablyTypedConverterExternalNpmPlugin)
    //.configure(baseSettings, browserProject, reactNpmDeps, bundlerSettings, withCssLoading)
    .settings(
      //addCommandAlias("graph-viewer", "project graph-viewer;fastOptJS::startWebpackDevServer;~fastOptJS"),
      scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) },
      scalaJSUseMainModuleInitializer := true,
      scalaVersion := "2.13.6",
      externalNpm := {
        Process("yarn", baseDirectory.value).!
        baseDirectory.value
      },
      webpackDevServerPort := 8008,
      stFlavour            := Flavour.Japgolly,
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
      libraryDependencies ++= Seq("me.shadaj" %%% "slinky-hot" % "0.6.7"),
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
      Compile / npmDependencies ++= NpmDependencies.`journal`
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
      libraryDependencies ++= Seq("me.shadaj" %%% "slinky-hot" % "0.6.7"),
      Compile / npmDependencies ++= NpmDependencies.`dashboard`
    )


lazy val `expense-tracker-native` =
  (project in file("modules/expense-tracker-native"))
    .enablePlugins(ScalaJSPlugin)
    .enablePlugins(ScalablyTypedConverterExternalNpmPlugin)
    .settings(
      scalaVersion := "2.13.6",
      scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)),
      scalaJSUseMainModuleInitializer := false,
      /* ScalablyTypedConverterExternalNpmPlugin requires that we define how to install node dependencies and where they are */
      externalNpm := {
        Process("yarn", baseDirectory.value).!
        baseDirectory.value
      },
      stFlavour := Flavour.Japgolly,
      run := {
        (Compile / fastOptJS).value
        Process("expo start", baseDirectory.value).!
      },
      libraryDependencies ++= Seq(
        "com.softwaremill.sttp.client" %%% "core"          % "2.2.9",
        "com.softwaremill.sttp.client" %%% "circe"         % "2.2.9",
        "io.circe"                     %%% "circe-optics"  % "0.14.1",
        "io.circe"                     %%% "circe-generic" % "0.14.1"
      )
    )

val yarnBuild     = taskKey[Unit]("fullOptJS then webpack")
val yarnBuildFast = taskKey[Unit]("fastOptJS then webpack")
val yarnRunDemo   = taskKey[Unit]("fastOptJS then run webpack server")
lazy val laminar = (project in file("modules/laminar"))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalablyTypedConverterExternalNpmPlugin)
  .settings(
    scalaVersion := "2.13.6",
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) },
    scalaJSUseMainModuleInitializer := true,
    externalNpm := {
      Process("yarn", baseDirectory.value).!
      baseDirectory.value
    },
    libraryDependencies += "com.raquo" %%% "laminar" % "0.12.1",
    // Watch non-scala assets, when they change trigger sbt
    // if you are using ~npmBuildFast, you get a rebuild
    // when non-scala assets change
    watchSources += baseDirectory.value / "public",
    yarnBuild := {
      (fullOptJS in Compile)
      "yarn --cwd modules/laminar/ run app" !
    },
    yarnBuildFast := {
      (fastOptJS in Compile)
      "yarn --cwd modules/laminar/ run app:dev" !
    },
    yarnRunDemo := {
      (fastOptJS in Compile)
      "yarn --cwd modules/laminar/ run app:dev-start" !
    }
  )

lazy val `login-native` =
  (project in file("modules/login-native"))
    .enablePlugins(ScalaJSPlugin)
    .enablePlugins(ScalablyTypedConverterExternalNpmPlugin)
    .settings(
      scalaVersion := "2.13.6",
      scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)),
      scalaJSUseMainModuleInitializer := false,
      /* ScalablyTypedConverterExternalNpmPlugin requires that we define how to install node dependencies and where they are */
      externalNpm := {
        Process("yarn", baseDirectory.value).!
        baseDirectory.value
      },
      stFlavour := Flavour.Japgolly,
      run := {
        (Compile / fastOptJS).value
        Process("expo start", baseDirectory.value).!
      },
      libraryDependencies ++= Seq(
        "com.softwaremill.sttp.client" %%% "core"          % "2.2.9",
        "com.softwaremill.sttp.client" %%% "circe"         % "2.2.9",
        "io.circe"                     %%% "circe-optics"  % "0.14.1",
        "io.circe"                     %%% "circe-generic" % "0.14.1"
      )
    )

lazy val `pokedex-native` =
  (project in file("modules/pokedex-native"))
    .enablePlugins(ScalaJSPlugin)
    .enablePlugins(ScalablyTypedConverterExternalNpmPlugin)
    .settings(
      scalaVersion := "2.13.6",
      scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)),
      scalaJSUseMainModuleInitializer := false,
      externalNpm := {
        Process("yarn", baseDirectory.value).!
        baseDirectory.value
      },
      stFlavour := Flavour.Japgolly,
      run := {
        (Compile / fastOptJS).value
        Process("yarn react-native start", baseDirectory.value).!
      }
    )

lazy val `simple-test` =
  (project in file("modules/simple-test"))
    .enablePlugins(ScalablyTypedConverterPlugin)
    .enablePlugins(ScalaJSPlugin)
    .configure(testConfiguration, reactNpmDeps, browserProject)
    .settings(
      addCommandAlias("simple-test", "project simple-test;fastOptJS::startWebpackDevServer;~fastOptJS"),
      scalaVersion := "2.13.6",
      useYarn      := true,
      Compile / npmDependencies ++= NpmDependencies.`simple-test`,
      Compile / npmDevDependencies ++= Seq(
        "file-loader"         -> "6.0.0",
        "style-loader"        -> "1.2.1",
        "css-loader"          -> "3.5.3",
        "html-webpack-plugin" -> "4.3.0",
        "copy-webpack-plugin" -> "5.1.1",
        "webpack-merge"       -> "4.2.2"
      ),
      libraryDependencies ++= Seq(
        "me.shadaj"     %%% "slinky-hot" % "0.6.7",
        "org.scalatest" %%% "scalatest"  % "3.2.9" % Test
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
    Compile / npmDevDependencies ++= NpmDependencies.`withCssLoading`
  )

lazy val reactNpmDeps: Project => Project =
  _.settings(
    Compile / npmDependencies ++= NpmDependencies.`reactNpmDeps`
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
      scalaVersion := "2.13.6",
      scalacOptions ++= ScalacOptions.flags,
      scalacOptions += "-Ymacro-annotations",
      scalaJSUseMainModuleInitializer := true,
      scalaJSLinkerConfig ~= (/* disabled because it somehow triggers many warnings */
      _.withSourceMap(false)
        .withModuleKind(ModuleKind.CommonJSModule)),
      libraryDependencies ++= Seq(
        "dev.zio"                      %%% "zio"             % "1.0.11",
        "io.github.cquiroz"            %%% "scala-java-time" % "2.3.0",
        "org.scalatest"                %%% "scalatest"       % "3.2.9" % Test,
        "com.softwaremill.sttp.client" %%% "core"            % "2.2.9",
        "com.softwaremill.sttp.client" %%% "circe"           % "2.2.9",
        "io.circe"                     %%% "circe-optics"    % "0.14.1",
        "io.circe"                     %%% "circe-generic"   % "0.14.1"
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

      println(artifacts)
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
