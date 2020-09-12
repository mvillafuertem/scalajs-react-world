package io.github.mvillafuertem.components

import java.time.LocalDate

import org.scalablytyped.runtime.StringDictionary
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import typings.recharts.components._
import typings.recharts.rechartsStrings.monotone

import scala.scalajs.js

// https://codesandbox.io/s/n95n2wpp6l?file=/src/BarGraph.js
@react object SimpleBarChart {

  class Category(val name : String, val value: Double, val avg: Double)
  class Data(val date: LocalDate,
             val squat: Double,
             val inclinebenchpress: Double,
             val hipthrust: Double,
             val dumbbellrow: Double,
             val calfmachine: Double,
            ) extends js.Object

  class DataDBO(val date: String,
                val squat: Double,
                val inclinebenchpress: Double,
                val hipthrust: Double,
                val dumbbellrow: Double,
                val calfmachine: Double,
               ) extends js.Object

  val data: js.Array[js.Object] = js.Array(
    new Data(LocalDate.of(2020, 8, 1), 40, 43, 230.25, 200, 240),
    new Data(LocalDate.of(2020, 8, 20), 40, 43, 230.25, 200, 130),
    new Data(LocalDate.of(2020, 8, 15), 128, 298, 126.7, 200, 220),
    new Data(LocalDate.of(2020, 8, 16), 500, 430, 320.6, 432, 180),
    new Data(LocalDate.of(2020, 8, 17), 470, 208, 140, 200, 168.6),
    new Data(LocalDate.of(2020, 8, 12), 470, 208, 140, 200, 192.25),
    new Data(LocalDate.of(2020, 8, 18), 90, 40, 140, 240, 112.5),
    new Data(LocalDate.of(2020, 8, 13), 200, 120, 140, 200, 123.4),
    new Data(LocalDate.of(2020, 8, 19), 39, 38, 142.5, 230, 135.75)
  ).sortBy(_.date).map(data => new DataDBO(data.date.toString, data.squat, data.inclinebenchpress, data.hipthrust, data.dumbbellrow, data.calfmachine))



  type Props = Unit

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] {
    _ =>
      ResponsiveContainer
        .width("99%")
        .height(320)(
          BarChart.data(data)(
            XAxis.dataKey("date"),
            YAxis(),
            CartesianGrid.vertical(false).strokeDasharray("3 3"),
            Tooltip(),
            Legend(),
              Bar("squat").withKey("1").fill("#8884d8" ),
              Bar("inclinebenchpress").withKey("1").fill("#8884d8" ),
              Bar("hipthrust").withKey("1").fill("#8884d8" ),
              Bar("dumbbellrow").withKey("1").fill("#8884d8" ),
              Bar("calfmachine").withKey("1").fill("#8884d8" ),
          )
        )
  }
}
