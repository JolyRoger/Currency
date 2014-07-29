package com.currency

import scala.xml.Null

/**
 * Created by daniil.monakhov on 24.07.2014.
 */
class CurrencyData {
  var date = ""
  var usdBuy = 0.0
  var usdSell = 0.0
  var eurBuy = 0.0
  var eurSell = 0.0

  def getDate = date
  def getUsdBuy = usdBuy
  def getUsdSell = usdSell
  def getEurBuy = eurBuy
  def getEurSell = eurSell

  override def toString =
    "date: " + date + " usdBye=" + usdBuy + " usdSell=" + usdSell + " eurBye=" + eurBuy + " eurSell=" + eurSell
}

object CurrencyData {
  def parse(data: String) = {
    val dataArray: Array[String] = data.replace('/', ' ').replace(',', '.').replace('-', '.').split(" ")
    val cd: CurrencyData = new CurrencyData
    if (dataArray.length >= 5) {
      try {
        cd.date = dataArray(0)
        cd.usdBuy = dataArray(1).toDouble
        cd.usdSell = dataArray(2).toDouble
        cd.eurBuy = dataArray(3).toDouble
        cd.eurSell = dataArray(4).toDouble
      }
      catch {
        case e: NumberFormatException => {
          e.printStackTrace
          Null
        }
      }
    }
    else Null
    cd
  }
}
