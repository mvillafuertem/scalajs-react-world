package io.github.mvillafuertem

import io.github.mvillafuertem.component.VisNetworkReactComponent
import io.github.mvillafuertem.domain.{Edge, Node}
import io.github.mvillafuertem.facade._
import org.scalajs.dom.console
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.{Hooks, SetStateHookCallback}
import slinky.web.html.{button, div, hr, onClick}

import scala.scalajs.js

@react object Graph {

  val nodesDefault = new DataSet(
    js.Array(
      Node(1, "Node 1", level = 1, group = 1),
      Node(2, "Node 2", level = 2, group = 2),
      Node(3, "Node 3", level = 2, group = 3),
      Node(4, "Node 4", level = 3, group = 4),
      Node(5, "Node 5", level = 3, group = 5),
      Node(6, "Node 6", level = 4, group = 6),
      Node(7, "Node 7", level = 4, group = 7),
      Node(11, "property.name", 0, shape = "box", group = 1),
      Node(12, "property.phone", 0, shape = "box", group = 1),
      Node(13, "property.email", 0, shape = "box", group = 1)
    )
  )

  val edgesDefault = new DataSet(
    js.Array(
      Edge(1, 2),
      Edge(1, 3),
      Edge(2, 4),
      Edge(2, 5),
      Edge(4, 6),
      Edge(5, 6),
      Edge(5, 7),
      Edge(1, 11, arrows = ""),
      Edge(1, 12, arrows = ""),
      Edge(1, 13, arrows = "")
    )
  )

  val dataDefault: js.Dictionary[DataSet] = js.Dictionary[DataSet](
    "nodes" -> nodesDefault,
    "edges" -> edgesDefault
  )

  type Props = Unit

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { _ =>
    val (network, setNetwork) = Hooks.useState[Network[DataSet]](js.Object.asInstanceOf[Network[DataSet]])
    val (data, setData)       = Hooks.useState[js.Dictionary[DataSet]](dataDefault)

    val handleCreate: js.Function0[Unit] = () => network.addNodeMode()
    val handleEdit:   js.Function0[Unit] = () => console.log(network.getPositions())
    val handleDelete: js.Function0[Unit] = () => network.deleteSelected()

    val getNetwork: js.Function1[Network[DataSet], Unit] = setNetwork(_)

    div()(
      button(onClick := handleCreate)("create"),
      button(onClick := handleEdit)("edit"),
      button(onClick := handleDelete)("delete"),
      hr(),
      VisNetworkReactComponent.component(
        VisNetworkReactComponent.Props(
          data = data,
          getNetwork = Some(getNetwork)
        )
      )
    )
  }

}
