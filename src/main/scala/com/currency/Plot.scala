package com.currency

import java.awt.Font
import java.awt.event.{MouseWheelEvent, MouseWheelListener, MouseAdapter}
import java.text.SimpleDateFormat

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

object Plot extends MainFrame with scala.swing.Reactor /*with App*/ {
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
      xAxis.setDateFormatOverride(df)
      xAxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 1))

      val yAxis: ValueAxis = new NumberAxis("Rate")
      yAxis.setRange(35.0, 37.5)

      val renderer: XYItemRenderer = new XYLineAndShapeRenderer
      val plot: XYPlot = new XYPlot(dataset,
        xAxis, yAxis, renderer)
      val chart: JFreeChart = new JFreeChart(legend, new Font("Tahoma", Font.PLAIN, 18), plot, true)
      val chartPanel: ChartPanel = new ChartPanel(chart)
//      chartPanel.setPreferredSize(new Dimension(1280, 800))
      chartPanel.setPreferredSize(new Dimension(640, 480))

      chartPanel.addMouseListener(new MouseAdapter {
        override def mouseClicked(e: java.awt.event.MouseEvent) {
        }
      })
      chartPanel.addMouseWheelListener(new MouseWheelListener {
        override def mouseWheelMoved(e: MouseWheelEvent) {
          println(" prWhRot:" + e.getPreciseWheelRotation + "  WhRot:" + e.getWheelRotation + " ScrT:" + e.getScrollType + " ScrAm:" + e.getScrollAmount
          + " UnTScr:" + e.getUnitsToScroll)
          def rangeValue =  e.getUnitsToScroll / 10d
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
            CurrencyData.parse("2014-08-11 35,40/36,65 47,40/49,10") :: Nil
  show("Raiffeisen currency rate Raiffeisen USD and EUR exchange currency rate by the date", lst)
}
