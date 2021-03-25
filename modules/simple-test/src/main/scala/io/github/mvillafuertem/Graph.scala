package io.github.mvillafuertem

import io.github.mvillafuertem.facade.{ DataSet, Network }
import org.scalajs.dom
import org.scalajs.dom.console
import slinky.core.Component
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html.{ div, id, style }

import scala.scalajs.js
import scala.scalajs.js.|
import scala.util.Random

@react class Graph extends Component {

  type Props = Unit

  val colors: js.Array[String] = js.Array[String](
    "#69d2e7",
    "#a7dbd8",
    "#e0e4cc",
    "#f38630",
    "#fa6900",
    "#fe4365",
    "#fc9d9a",
    "#f9cdad",
    "#c8c8a9",
    "#83af9b",
    "#ecd078",
    "#d95b43",
    "#c02942",
    "#53777a"
  )

  def newNode(id: Int, label: String, color: String, title: String, group: Int, shape: String = "dot"): js.Dictionary[Any] =
    js.Dictionary("id" -> id, "label" -> label, "color" -> color, "title" -> title, "group" -> group, "shape" -> shape)

  def newEdges(from: Int, to: Int, arrows: String = "to"): js.Dictionary[Any] =
    js.Dictionary("from" -> from, "to" -> to, "arrows" -> arrows)

  // create an array with nodes
  val nodes = new DataSet(
    js.Array(
      // Nodes
      newNode(1, "Node 1", colors(0), "Title Node 1", 1),
      newNode(2, "Node 2", colors(1), "Title Node 2", 2),
      // Properties
      newNode(11, "id", colors(0), "Property Node 1", 1, "box"),
      newNode(12, "name", colors(0), "Property Node 1", 1, "box"),
      newNode(13, "information.street", colors(0), "Property Node 1", 1, "box"),
      newNode(14, "information.phone", colors(0), "Property Node 1", 1, "box"),
      newNode(21, "device.id", colors(1), "Property Node 1", 2, "box"),
      newNode(22, "device.name", colors(1), "Property Node 1", 2, "box"),
      newNode(23, "street", colors(1), "Property Node 1", 2, "box"),
      newNode(24, "phone", colors(1), "Property Node 1", 2, "box")
      //newNode(4, "Node 4", colors(3), "Title Node 4", 3),
      //newNode(5, "Node 5", colors(4), "Title Node 5", 3),
      //newNode(6, "Node 6", colors(5), "Title Node 6", 3)
    )
  )

  // create an array with edges
  var edges = new DataSet(
    js.Array(
      newEdges(from = 1, to = 2),
      newEdges(from = 11, to = 1, ""),
      newEdges(from = 12, to = 1, ""),
      newEdges(from = 13, to = 1, ""),
      newEdges(from = 14, to = 1, ""),
      newEdges(from = 21, to = 2, ""),
      newEdges(from = 22, to = 2, ""),
      newEdges(from = 23, to = 2, ""),
      newEdges(from = 24, to = 2, "")
      //newEdges(from = 2, to = 4),
      //newEdges(from = 2, to = 5),
      //newEdges(from = 3, to = 6)
    )
  )

  val nodesOptions = js.Dictionary(
    "borderWidth" -> 3,
    "font" -> js.Dictionary(
      "align" -> "center",
      "color" -> "#080808",
      "face"  -> "Courier New",
      "size"  -> 14
    ),
    "shape" -> "dot",
    "size"  -> 15
  )

  val edgesOptions = js.Dictionary(
    "color" -> js.Dictionary(
      "inherit" -> false,
      "color"   -> "#222424"
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
    "chosen" -> false
  )

  val options: js.Dictionary[Any] = js.Dictionary(
    "autoResize" -> true,
    "height"     -> "100%",
    "width"      -> "100%",
    "nodes"      -> nodesOptions,
    "edges"      -> edgesOptions,
    "layout"     -> js.Dictionary("hierarchical" -> js.Dictionary("direction" -> "LR")),
    "physics"    -> false
  )

  override def componentDidMount(): Unit = {
    val network: Network[DataSet] = Network(
      container = dom.document.getElementById(s"graph"),
      data = js.Dictionary[DataSet](
        "nodes" -> nodes,
        "edges" -> edges
      ),
      options = options
    )
    //network.clusterOutliers()
    val clusterOptionsByData = js.Dynamic.literal(
      "joinCondition" -> (((childOptions: js.Dynamic) => {

        console.log(childOptions.group)
        childOptions.group.asInstanceOf[Int] == 1

      }): js.Function1[js.Dynamic, Boolean]),
      "clusterNodeProperties" -> js.Dictionary(
        "label"       -> "Cluster 1",
      )
    )
    network.cluster(clusterOptionsByData)

    network.on(
      "selectNode",
      (params: js.UndefOr[Any]) => {
        val nodes = params.asInstanceOf[js.Dynamic].nodes.asInstanceOf[js.Array[js.Any]]
        if (nodes.length == 1) {
          if (network.isCluster(nodes(0).asInstanceOf[String | Int]) == true) {
            network.openCluster(nodes(0).asInstanceOf[String | Int])
          }
        }

      }
    )
  }

  override def render(): ReactElement =
    div(
      id := s"graph",
      style := js.Dynamic.literal(
        /* Height and width fallback for older browsers. */
        "height" -> "100%",
        "width"  -> "100%",
        /* Set the height to match that of the viewport. */
        "height" -> "100vh",
        /* Set the width to match that of the viewport. */
        "width" -> "100vw",
        /* Remove any browser-default margins. */
        "margin" -> 0
      )
    )()

  override type State = Unit

  override def initialState: Unit = ()
}
