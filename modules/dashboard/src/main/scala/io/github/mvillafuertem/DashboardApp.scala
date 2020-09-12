package io.github.mvillafuertem

import io.github.mvillafuertem.components.{ AppTheme, Dashboard }
import org.scalajs.dom.document
import slinky.core.FunctionalComponent
import slinky.core.facade.Fragment
import slinky.web.ReactDOM
import typings.materialUiCore.createMuiThemeMod.{Theme, ThemeOptions}
import typings.materialUiCore.createTypographyMod.TypographyOptions
import typings.materialUiCore.stylesMod
import typings.materialUiCore.stylesMod.createMuiTheme
import typings.materialUiStyles.components.ThemeProvider
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{BrowserRouter, Link, Redirect, Route}
import zio.{App, ExitCode, Task, ZIO}
import org.scalajs.dom
import slinky.core.FunctionalComponent
import slinky.core.facade.Fragment
import slinky.web.ReactDOM
import typings.materialUiCore.components.{List, ListItem, ListItemIcon, ListItemText, ListSubheader, Typography}
import typings.materialUiCore.createMuiThemeMod.{Theme, ThemeOptions}
import typings.materialUiCore.createTypographyMod.TypographyOptions
import typings.materialUiCore.stylesMod.createMuiTheme
import typings.materialUiCore.typographyTypographyMod.Style
import typings.materialUiIcons.{components => Icon}
import typings.materialUiStyles.components.ThemeProvider
import typings.reactRouter.mod.RouteProps

object DashboardApp extends App {

  val theme: Theme = stylesMod.createMuiTheme(
    ThemeOptions()
      .setTypography(TypographyOptions().setUseNextVariants(true)) // https://v3.material-ui.com/style/typography/#migration-to-typography-v2
  )

  type Props = Unit

  /* the production build is deployed at github pages under /material-ui , while dev build is server from root of webpack-dev-server */
  val basename: String = if (scala.scalajs.runtime.linkingInfo.productionMode) "/zio-scala-js-material-ui/ui/" else ""

  val Main: FunctionalComponent[Props] = FunctionalComponent[Props] {
    case () =>
      ThemeProvider(theme)(
        BrowserRouter.basename(basename)(
          Route(
            RouteProps()
              .setExact(true)
              .setPath("/")
              .setRender(_ => List(Redirect(to = "/dashboard")()))
          ),
          Route(RouteProps().setPath("/dashboard").setRender(_ => AppTheme(title = "Dashboard page layout example - Material-UI", description = "An example layout for creating an albumn.", hideCredit = true)(Dashboard()))),
        )
      )
  }

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    Task
      .effectSuspendTotal(
        Task.effect(
          ReactDOM
            .render(Main(()), document.getElementById("container"))
        )
      )
      .exitCode

}
