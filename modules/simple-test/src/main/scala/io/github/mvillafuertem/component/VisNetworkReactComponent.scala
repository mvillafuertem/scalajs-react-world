package io.github.mvillafuertem.component

import io.github.mvillafuertem.domain.Node
import io.github.mvillafuertem.facade.Network.IdType
import io.github.mvillafuertem.facade.{ DataSet, Network }
import org.scalajs.dom.console
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Hooks
import slinky.core.facade.Hooks.useEffect
import slinky.web.html.{ div, ref, style }

import scala.scalajs.js
import scala.scalajs.js.|
import scala.util.Random

@react object VisNetworkReactComponent {

  private val nodesOptions: js.Dictionary[Any] = js.Dictionary(
    "color"       -> Node.colors(Random.between(0, 13)),
    "borderWidth" -> 3,
    "font" -> js.Dictionary(
      "align" -> "center",
      "color" -> "#080808",
      "face"  -> "Courier New",
      "size"  -> 14
    ),
    "shape" -> "dot",
    "size"  -> 30
  )

  private val edgesOptions: js.Dictionary[Any] = js.Dictionary(
    "color" -> js.Dictionary(
      "inherit"   -> false,
      "color"     -> "#222424",
      "highlight" -> "red",
      "hover"     -> "blue"
    ),
    "smooth" -> js.Dictionary(
      "type"           -> "cubicBezier",
      "forceDirection" -> "horizontal",
      "roundness"      -> 0.4
    ),
    "arrows" -> js.Dictionary(
      "to" -> js.Dictionary(
        "enabled"     -> false,
        "scaleFactor" -> 1,
        "type"        -> "arrow"
      ),
      "middle" -> js.Dictionary(
        "enabled"     -> false,
        "imageHeight" -> 32,
        "imageWidth"  -> 32,
        "scaleFactor" -> 1,
        "src"         -> "https://visjs.org/images/visjs_logo.png",
        "type"        -> "image"
      ),
      "from" -> js.Dictionary(
        "enabled"     -> false,
        "scaleFactor" -> 1,
        "type"        -> "arrow"
      )
    ),
    "chosen" -> true
  )

  private val defaultOptions: js.Dictionary[Any] = js.Dictionary(
    "autoResize" -> true,
    "nodes"      -> nodesOptions,
    "edges"      -> edgesOptions,
    "layout"     -> js.Dictionary("hierarchical" -> js.Dictionary("direction" -> "LR")),
    "physics"    -> false,
    "groups" -> js.Dictionary(
      "1" -> js.Dictionary(
        "color" -> Node.colors(Random.between(0, 13))
      )
    ),
    "manipulation" -> js.Dictionary(
      "enabled" -> false,
      "addNode" -> ((
        (
          nodeData: Any,
          callback: js.Function1[Any, Any]
        ) => {
          // TODO ADD REDUX LOGIC
          console.log(nodeData)
          console.log(callback)
          nodeData.asInstanceOf[js.Dynamic].label = "hello world"
          nodeData.asInstanceOf[js.Dynamic].level = 0
          callback(nodeData)
        }
      ): js.Function2[Any, js.Function1[Any, Any], Unit]),
      "deleteEdge" -> ((
        (
          edgeData: Any,
          callback: js.Function1[Any, Any]
        ) => {
          console.log(edgeData)
          console.log(callback)
          callback(edgeData)
        }
      ): js.Function2[Any, js.Function1[Any, Any], Unit])
    )
  )

  case class Props(
    data:    js.Dictionary[DataSet],
    options: js.Dictionary[Any] = defaultOptions,
    style: js.Object = js.Dynamic.literal(
      "width"  -> "100%",
      "height" -> "100%"
    ),
    getNetwork: Option[js.Function1[Network[DataSet], Unit]] = None,
    getNodes:   Option[js.Function1[DataSet, Unit]] = None
  )

  val clusterOptionsByData: js.Function1[Unit, js.Dynamic] = (_) =>
    js.Dynamic.literal(
      "joinCondition" -> (((childOptions: js.Dynamic) => {
        console.log(childOptions)
        childOptions.group.asInstanceOf[String | Int] == "1"
      }): js.Function1[js.Dynamic, Boolean]),
      "clusterNodeProperties" -> js.Dictionary(
        "label" -> "Cluster 1",
        "level" -> 0
        //"color" -> color
      )
    )

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    val network   = Hooks.useRef[Network[DataSet]](null)
    val container = Hooks.useRef(null)

    useEffect(
      { () =>
        network.current = Network(
          container = container.current,
          data = props.data,
          options = props.options
        )

        network.current.cluster(clusterOptionsByData())

        network.current.on("zoom", (params: js.UndefOr[Any]) => network.current.cluster(clusterOptionsByData()))

        network.current.on(
          "selectNode",
          (params: js.UndefOr[Any]) => {
            val nodes = params.asInstanceOf[js.Dynamic].nodes.asInstanceOf[js.Array[js.Any]]
            console.log(network.current.getConnectedNodes(nodes(0).asInstanceOf[IdType]))
            if (nodes.length == 1) {
              if (network.current.isCluster(nodes(0).asInstanceOf[IdType])) {
                network.current.openCluster(nodes(0).asInstanceOf[IdType])
              }
            }
          }
        )

//        network.current.on(
//          "selectEdge",
//          (params: js.UndefOr[Any]) => console.log("selectEdge")
//        )

        props.getNetwork.map(f => f(network.current))
      },
      js.Array(network)
    )

    div(
      ref := container,
      style := js.Dynamic.literal(
        /* Height and width fallback for older browsers. */
        "height" -> "100%",
        "width"  -> "50%",
        /* Set the height to match that of the viewport. */
        "height" -> "90vh",
        /* Set the width to match that of the viewport. */
        "width" -> "80vw",
        /* Remove any browser-default margins. */
        "margin" -> "0 auto",
        "border" -> "1px solid"
      )
    )
  }

}
