package com.currency

import java.awt.Font
import java.awt.event._
import java.awt.geom.Rectangle2D
import java.text.SimpleDateFormat
import java.util.{GregorianCalendar, Calendar, Date}

import org.jfree.chart.{ChartPanel, JFreeChart}
import org.jfree.chart.axis._
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.{XYLineAndShapeRenderer, XYItemRenderer}
import org.jfree.data.xy.{XYSeriesCollection, XYSeries}

import scala.swing.event._
import scala.swing._
/**
 * Created by daniil.monakhov on 13.08.2014.
 */

object Plot extends MainFrame with scala.swing.Reactor/* with App*/ {
  val currencyMin = 38.0
  val currencyMax = 42.5
  val dateMin = "2014.09.29"
  val dateMax = new Date

  def show(title: String, list: List[CurrencyData]) {
      this.title = title;
      val legend: String = "Raiffeisen USD and EUR exchange currency rate by the date"
      val df = new SimpleDateFormat("yyyy.MM.dd")

      val usdSellSeries = new XYSeries("USD Sell")
      list.foreach(cd => { usdSellSeries.add(df.parse(cd.getDate).getTime, cd.getUsdSell)})
      val dataset = new XYSeriesCollection(usdSellSeries)

      val usdBuySeries = new XYSeries("USD Buy")
      list.foreach(cd => { usdBuySeries.add(df.parse(cd.getDate).getTime, cd.getUsdBuy)})
      dataset.addSeries(usdBuySeries)

      val eurSellSeries = new XYSeries("EUR Sell")
      list.foreach(cd => { eurSellSeries.add(df.parse(cd.getDate).getTime, cd.getEurSell)})
      dataset.addSeries(eurSellSeries)

      val eurBuySeries = new XYSeries("EUR Buy")
      list.foreach(cd => { eurBuySeries.add(df.parse(cd.getDate).getTime, cd.getEurBuy)})
      dataset.addSeries(eurBuySeries)

      val xAxis: DateAxis = new DateAxis("Date")

      xAxis.setRange(df.parse(dateMin), new Date)
      xAxis.setDateFormatOverride(df)
      xAxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 1))

      val yAxis: ValueAxis = new NumberAxis("Rate")
      yAxis.setRange(currencyMin, currencyMax)

      val renderer: XYItemRenderer = new XYLineAndShapeRenderer
      val plot: XYPlot = new XYPlot(dataset, xAxis, yAxis, renderer)
      val chart: JFreeChart = new JFreeChart(legend, new Font("Tahoma", Font.PLAIN, 18), plot, true)
      val chartPanel: ChartPanel = new ChartPanel(chart)

      chartPanel.setPreferredSize(new Dimension(1280, 800))
//      chartPanel.setPreferredSize(new Dimension(640, 480))

    val mouseAdapter = new MouseAdapter {
      var x = 0
      var y = 0

      override def mouseDragged(e: java.awt.event.MouseEvent) {
        if (e.getModifiers == 18) {
            val div = x.toDouble / e.getX
            val xRangeValue = if (x < e.getX) -2.88E7 else
              if (x > e.getX) 2.88E7 else 0L * div               // 8 hours in milliseconds
            val yRangeValue = if (y < e.getY) 0.1 else
              if (y > e.getY) -0.1 else 0 * div               // 8 hours in milliseconds
              (e.getY - y) / 10d
            x = e.getX
            y = e.getY
            xAxis.setRange(xAxis.getRange.getLowerBound + xRangeValue, xAxis.getRange.getUpperBound + xRangeValue)
            yAxis.setRange(yAxis.getRange.getLowerBound + yRangeValue, yAxis.getRange.getUpperBound + yRangeValue)
        }
      }

      override def mousePressed(e: java.awt.event.MouseEvent) {
        x = e.getX; y = e.getY
        val rect = chartPanel.getScreenDataArea(e.getX, e.getY)
        val point = getPointInRectangle(e.getX, e.getY, rect)
        val screenDataArea = chartPanel.getScreenDataArea(math.round(point._1).toInt, math.round(point._2).toInt)
      }

      def getPointInRectangle(x: Int, y: Int, area: Rectangle2D) = {
        (math.max(math.ceil(area.getMinX), math.min(x, math.floor(area.getMaxX))),
        math.max(math.ceil(area.getMinY), math.min(y, math.floor(area.getMaxY))))
      }
    }



      chartPanel.addMouseMotionListener(mouseAdapter)
      chartPanel.addMouseListener(mouseAdapter)

      chartPanel.addMouseWheelListener(new MouseWheelListener {
        override def mouseWheelMoved(e: MouseWheelEvent) {
          def rangeValue =  -e.getUnitsToScroll / 10d
          yAxis.setRange(yAxis.getRange.getLowerBound + rangeValue, yAxis.getRange.getUpperBound + rangeValue)
        }
      })
      contents = new BorderPanel {
        peer.add(chartPanel)
      }
      this.visible = true
  }

  val lst = CurrencyData.parse("2014.08.13 35,60/36,85 47,55/49,20") ::
            CurrencyData.parse("2014-08-12 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-08-11 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-08-10 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-08-09 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-08-08 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-08-07 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-08-06 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-08-05 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-08-04 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-08-03 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-08-02 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-08-01 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-07-31 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-07-30 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-07-29 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-07-28 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-07-27 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-07-26 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-07-25 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-07-24 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-07-23 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-07-22 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-07-21 35,40/36,70 47,40/49,15") ::
            CurrencyData.parse("2014-07-20 35,40/36,65 47,40/49,10") :: Nil
  show("Raiffeisen currency rate Raiffeisen USD and EUR exchange currency rate by the date", lst)
}
