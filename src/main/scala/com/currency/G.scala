package com.currency

import java.awt.{Dimension, Font}
import java.text.SimpleDateFormat
import javax.swing.JFrame
import org.jfree.chart.{ChartPanel, JFreeChart}
import org.jfree.chart.axis._
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.{XYLineAndShapeRenderer, XYItemRenderer}
import org.jfree.data.xy.{XYSeriesCollection, XYSeries}

/**
  * Created by daniil.monakhov on 17.07.2014.
 */
object G extends JFrame {
  def showPlot(title: String, list: List[CurrencyData]) {
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
    yAxis.setRange(35.0, 37.0)
    val renderer: XYItemRenderer = new XYLineAndShapeRenderer
    val plot: XYPlot = new XYPlot(dataset, xAxis, yAxis, renderer)
    val chart: JFreeChart = new JFreeChart(legend, new Font("Tahoma", Font.PLAIN, 18), plot, true)
    val chartPanel: ChartPanel = new ChartPanel(chart)
    chartPanel.setPreferredSize(new Dimension(800, 600))

    setContentPane(chartPanel)
    setTitle(title)
    pack()
    setVisible(true)
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  }
}
