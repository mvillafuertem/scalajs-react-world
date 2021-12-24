package io.github.mvillafuertem

import io.github.mvillafuertem.components.{ AppTheme, Dashboard }
import org.scalajs.dom.document
import slinky.core.FunctionalComponent
import slinky.web.ReactDOM
import typings.materialUiCore.components.List
import typings.materialUiCore.createMuiThemeMod.{ Theme, ThemeOptions }
import typings.materialUiCore.createTypographyMod.TypographyOptions
import typings.materialUiCore.stylesMod
import typings.materialUiStyles.components.ThemeProvider
import typings.reactRouter.mod.RouteProps
import typings.reactRouterDom.components.{ HashRouter => Router, Redirect, Route }
import zio.{ App, ExitCode, Task, ZIO }

object DashboardApp extends App {

  val theme: Theme = stylesMod.createMuiTheme(
    ThemeOptions()
      .setTypography(TypographyOptions().setUseNextVariants(true)) // https://v3.material-ui.com/style/typography/#migration-to-typography-v2
  )

  type Props = Unit

  val Main: FunctionalComponent[Props] = FunctionalComponent[Props] { case () =>
    ThemeProvider(theme)(
      Router(
        Route(
          RouteProps()
            .setExact(true)
            .setPath("/")
            .setRender(_ => List(Redirect(to = "/dashboard")()))
        ),
        Route(
          RouteProps()
            .setPath("/dashboard")
            .setRender(_ =>
              AppTheme(title = "Dashboard page layout example - Material-UI", description = "An example layout for creating an albumn.", hideCredit = true)(
                Dashboard()
              )
            )
        )
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
