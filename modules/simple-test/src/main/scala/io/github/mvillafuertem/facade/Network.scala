package io.github.mvillafuertem.facade

import io.github.mvillafuertem.facade.Network.{ IdType, Selection }
import org.scalajs.dom.Element

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import scala.scalajs.js.|

// https://github.com/visjs/vis-network/blob/master/types/network/Network.d.ts
// https://stackoverflow.com/questions/57144478/how-to-replace-manipulation-gui-with-my-own-in-vis-js
@js.native
@JSGlobal("vis.Network")
class Network[T](container: Element, data: js.Dictionary[T], options: js.Dictionary[Any]) extends js.Object {

  def cluster(options: js.UndefOr[Any]): Unit = js.native

  def clusterOutliers(): Unit = js.native

  def destroy(): Unit = js.native

  def addNodeMode(): Unit = js.native

  def deleteSelected(): Unit = js.native

  def getClusteredEdges(baseEdgeId: IdType): js.Array[IdType] = js.native

  def getConnectedNodes(nodeId: IdType): Any = js.native

  def getNodesInCluster(clusterNodeId: IdType): js.Array[IdType] = js.native

  def getPositions(): Any = js.native

  def disableEditMode(): Any = js.native

  def getSelection(): Selection = js.native

  def isCluster(nodeId: IdType): Boolean = js.native

  def on(networkEvents: String, f: js.Function1[js.UndefOr[Any], Unit]): Unit = js.native

  def openCluster(nodeId: IdType, options: js.UndefOr[Any] = js.undefined): Unit = js.native

  def setData(data: js.Dictionary[T]): Unit = js.native

  def setOptions(options: js.Dictionary[Any]): Unit = js.native

  def startSimulation(): Any = js.native
}

object Network {

  def apply[T](container: Element, data: js.Dictionary[T], options: js.Dictionary[Any]): Network[T] =
    new Network(container, data, options)

  type IdType = String | Int

  trait Selection extends js.Object {
    val nodes: js.Array[Any]
    val edges: js.Array[Any]
  }

  object Selection {
    def unapply(arg: Selection): Option[(js.Array[Any], js.Array[Any])] =
      Some(arg.nodes, arg.edges)
  }

}
