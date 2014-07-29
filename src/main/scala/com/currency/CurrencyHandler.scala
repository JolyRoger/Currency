package com.currency

import org.joda.time.DateTime
import org.xml.sax.ContentHandler
import org.xml.sax.ext.LexicalHandler
//import scalax.chart.api._
//import scalax.chart.module.Charting

class CurrencyHandler extends ContentHandler with LexicalHandler {
  val cd = new CurrencyData
  def getCurrencyData = cd
  /** As seen from class CurrencyHandler, the missing signatures are as follows. * For convenience, these are usable
 as stub implementations. */ 

  var rankValut = false
  var usd = false
  var eur = false
//  var usdBuy = 0.0
//  var usdSell = 0.0
//  var eurBuy = 0.0
//  var eurSell = 0.0
  var isBuy = true
  
  // Members declared in org.xml.sax.ContentHandler 
  override def characters(ch: Array[Char], start: Int, length: Int) {
	  if (rankValut) {
	    val str = new String(ch)
	    if (usd && !str.trim.isEmpty) {
	      if (isBuy) {
	        cd.usdBuy = str.toFloat
	        isBuy = false
	      } else {
          cd.usdSell = str.toFloat
	    	  isBuy = true
	    	  usd = false
	      }
	    } else if (eur && !str.trim.isEmpty) {
	    	if (isBuy) {
          cd.eurBuy = str.toFloat
	    				isBuy = false
	    	} else {
          cd.eurSell = str.toFloat
	    				isBuy = true
	    				eur = false
	    	}
	    }
	    if (str == "USD") usd = true
	    if (str == "EUR") eur = true
	  }
  }
  // Members declared in com.currency.CurrencyHandler
  override def startElement(uri: String, localName: String, qName: String, atts: org.xml.sax.Attributes) {
	  if (localName == "div") {
		  for (i <- 0 to atts.getLength) {
			  if (atts.getLocalName(i) == "class" && atts.getValue(i) == "rank-valut") {
				  rankValut = true
			  }
		  }
	  }
  }


  override def endElement(uri: String, localName: String, qName: String) {
	  if (localName == "div" && rankValut) {
		  rankValut = false
	  }
  }

  override def endDocument() {}
  override def endPrefixMapping(prefix: String) {}
  override def ignorableWhitespace(ch: Array[Char], start: Int, length: Int) {}
  override def processingInstruction(target: String, data: String) {}
  override def setDocumentLocator(locator: org.xml.sax.Locator) {}
  override def skippedEntity(name: String) {}
  override def startDocument() {}
  override def startPrefixMapping(prefix: String, uri: String) {}
  
  override def comment(ch: Array[Char], start: Int, length: Int) {} 
  override def endCDATA() {} 
  override def endDTD() {}
  override def endEntity(name: String) {}
  override def startCDATA() {}
  override def startDTD(name: String, publicId: String, systemId: String) {} 
  override def startEntity(name: String) {}
}