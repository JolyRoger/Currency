package com.currency

import nu.validator.htmlparser.common.XmlViolationPolicy
import nu.validator.htmlparser.sax.HtmlParser
import org.joda.time.DateTime
import org.xml.sax.InputSource

object CurrencyParser extends App {
  val now = DateTime.now + ""
  val today = now.substring(0, now.indexOf("T")).replace('-', '.')
  val fileName = "currency.txt"
  val lst = IO.readFromFile(fileName).map(parse(_, today))

  if (lst.forall(_.getDate != today)) {
    val handler = new CurrencyHandler
    val parser = new HtmlParser(XmlViolationPolicy.ALLOW)
    parser.setContentHandler(handler)
    parser.setProperty("http://xml.org/sax/properties/lexical-handler", handler)
    parser.parse(new InputSource("http://www.raiffeisen.ru"))

    val cd = handler.getCurrencyData
    println("! New data: " + cd)
    IO.appendToFile(fileName, today + " " + "%.2f".format(cd.usdBuy) + "/" + "%.2f".format(cd.usdSell) + " " +
      "%.2f".format(cd.eurBuy) + "/" + "%.2f".format(cd.eurSell))
  }
//  G.showPlot("Raiffeisen currency rate Raiffeisen USD and EUR exchange currency rate by the date", lst)
  Plot.show("Raiffeisen currency rate Raiffeisen USD and EUR exchange currency rate by the date", lst)

  def parse(date: String, today: String) = {
    val currencyData = CurrencyData.parse(date)
    currencyData
  }
}