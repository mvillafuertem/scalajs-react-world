package io.github.mvillafuertem.counter.native

import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ CtorType, ScalaFnComponent }
import typings.react.mod.useState
import typings.reactNative.components.{ Text, View }
import typings.reactNative.mod.{ StyleSheet, TextStyle, ViewStyle }
import typings.reactNative.reactNativeStrings.center

import scala.scalajs.js
import scala.scalajs.js.|

object CounterScreen {

  sealed trait CounterStyleKey
  final case object Container extends CounterStyleKey
  final case object Title extends CounterStyleKey

  val styles: js.Map[CounterStyleKey, ViewStyle | TextStyle] = StyleSheet.create(
    js.Map[CounterStyleKey, ViewStyle | TextStyle](
      Container -> ViewStyle()
        .setFlex(1)
        .setJustifyContent(center),
      Title -> TextStyle()
        .setFontSize(40)
        .setTextAlign(center)
        .setTop(-15)
    )
  )

  type Props = Unit

  val component: Component[Props, CtorType.Nullary] = ScalaFnComponent[Props] { _ =>
    val js.Tuple2(count, setCount) = useState[Int](10)

    View.style(styles(Container).toViewStyle)(
      Text.style(styles(Title).toTextStyle)(s"Count $count"),
      FabComponent.component(FabComponent.Props("+1", () => setCount(count + 1))),
      FabComponent.component(FabComponent.Props("-1", () => setCount(count - 1), FabComponent.BottomLeft))
    )
  }

}
