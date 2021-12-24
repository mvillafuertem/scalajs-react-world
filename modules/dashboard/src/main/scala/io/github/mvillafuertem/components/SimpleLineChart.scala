package io.github.mvillafuertem.components

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import org.scalablytyped.runtime.StringDictionary
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import typings.recharts.components._
import typings.recharts.rechartsStrings.monotone

import scala.scalajs.js

// https://github.com/mui-org/material-ui/blob/v3.x/docs/src/pages/getting-started/page-layout-examples/dashboard/SimpleLineChart.js
@react object SimpleLineChart {

  class Data(val date: LocalDate, val squat: Double, val inclinebenchpress: Double, val hipthrust: Double, val dumbbellrow: Double, val calfmachine: Double)
      extends js.Object

  class DataDBO(val date: String, val squat: Double, val inclinebenchpress: Double, val hipthrust: Double, val dumbbellrow: Double, val calfmachine: Double)
      extends js.Object

  val data: js.Array[js.Object] = js
    .Array(
      new Data(LocalDate.of(2020, 8, 1), 40, 43, 230.25, 200, 240),
      new Data(LocalDate.of(2020, 8, 20), 40, 43, 230.25, 200, 130),
      new Data(LocalDate.of(2020, 8, 15), 128, 298, 126.7, 200, 220),
      new Data(LocalDate.of(2020, 8, 16), 500, 430, 320.6, 432, 180),
      new Data(LocalDate.of(2020, 8, 17), 470, 208, 140, 200, 168.6),
      new Data(LocalDate.of(2020, 8, 12), 470, 208, 140, 200, 192.25),
      new Data(LocalDate.of(2020, 8, 18), 90, 40, 140, 240, 112.5),
      new Data(LocalDate.of(2020, 8, 13), 200, 120, 140, 200, 123.4),
      new Data(LocalDate.of(2020, 8, 19), 39, 38, 142.5, 230, 135.75)
    )
    .sortBy(_.date)
    .map(data => new DataDBO(data.date.toString, data.squat, data.inclinebenchpress, data.hipthrust, data.dumbbellrow, data.calfmachine))

  type Props = Unit

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { _ =>
    ResponsiveContainer
      .width("99%")
      .height(320)(
        LineChart.data(data)(
          XAxis.dataKey("date"),
          YAxis(),
          CartesianGrid.vertical(false).strokeDasharray("3 3"),
          Tooltip(),
          Legend(),
          Line("squat").`type`(monotone).stroke("#82ca9d").activeDot(StringDictionary("r" -> 8)).strokeWidth(2),
          Line("inclinebenchpress").`type`(monotone).stroke("#8884d8").activeDot(StringDictionary("r" -> 8)).strokeWidth(2),
          Line("hipthrust").`type`(monotone).stroke("#0088FE").activeDot(StringDictionary("r" -> 8)).strokeWidth(2),
          Line("dumbbellrow").`type`(monotone).stroke("#00C49F").activeDot(StringDictionary("r" -> 8)).strokeWidth(2),
          Line("calfmachine").`type`(monotone).stroke("#FFBB28").activeDot(StringDictionary("r" -> 8)).strokeWidth(2)
        )
      )
  }
}
