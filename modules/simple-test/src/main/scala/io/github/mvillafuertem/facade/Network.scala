package io.github.mvillafuertem.facade

import io.github.mvillafuertem.facade.Network.IdType
import org.scalajs.dom.Element

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import scala.scalajs.js.|





@js.native
@JSGlobal("vis.Network")
class Network[T](container: Element, data: js.Dictionary[T], options: js.Dictionary[Any]) extends js.Object {

  def destroy(): Unit = js.native
  def setData(data:       js.Dictionary[T]):   Unit = js.native
  def setOptions(options: js.Dictionary[Any]): Unit = js.native
  def clusterOutliers(): Unit = js.native
  def on(networkEvents:   String, f: js.Function1[js.UndefOr[Any], Unit]): Unit = js.native
  def isCluster(nodeId:   IdType): Boolean = js.native
  def openCluster(nodeId: IdType, options: js.UndefOr[Any] = js.undefined): Unit = js.native
  def cluster(options: js.UndefOr[Any]): Unit = js.native
}

object Network {

  def apply[T](container: Element, data: js.Dictionary[T], options: js.Dictionary[Any]): Network[T] =
    new Network(container, data, options)

  type IdType = String | Int
}
