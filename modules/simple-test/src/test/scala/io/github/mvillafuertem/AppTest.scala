package io.github.mvillafuertem

import org.scalajs.dom.document
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.web.{ReactDOM, html}
import slinky.web.html.{aria, div, role, span}
import typings.materialUiCore.components.Typography
import typings.materialUiCore.materialUiCoreStrings.center
import typings.materialUiCore.withThemeMod.WithTheme

class AppTest extends AnyFlatSpec with Matchers {

  behavior of "AppTest"

  it should "Renders without crashing" in {
    val div = document.createElement("div")
    ReactDOM.render(App(), div)
    ReactDOM.unmountComponentAtNode(div)
  }

  it should "Renders a single element into the DOM" in {
    val target = document.createElement("div")
    ReactDOM.render(
      html.a(),
      target
    )

    target.innerHTML shouldBe  "<a></a>"
  }

  it should "my component" in {
    val target = document.createElement("div")

    ReactDOM.render(
      Typography
        .align(center)(
          "Built with ",
          span(role := "img", aria-"label" := "Love")("❤️"),
          " by the",
          " team."
        ),
      target
    )

    target.innerHTML shouldBe  """<p class="MuiTypographyroot-0-1-40 MuiTypographybody1-0-1-49 MuiTypographyalignCenter-0-1-63">Built with <span role="img" aria-label="Love">❤️</span> by the team.</p>"""
  }

}



object AppTest {

  @react object MyComponent {
    case class Props(withTheme: WithTheme)
    val component: FunctionalComponent[Props] = FunctionalComponent[Props](props =>
      div(props.withTheme.theme.direction.toString))
  }

}
