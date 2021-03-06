package stockcharts.simulation.enrichers.indicators

import akka.stream.scaladsl.Source
import stockcharts.StockchartsTest
import stockcharts.models.{Money, Price}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class IndicatorsTest extends StockchartsTest {
  import stockcharts.simulation.enrichers.indicators.IndicatorsSupport._

  "Indicator calculations" should "work properly" in {
    val prices = (1 to 10).map(i => Price(today.plusDays(i), Money.zero, Money.zero, Money.zero, close = Money(i)))
    val smaPeriod = 2

    val closePrices = prices.map(_.close)
    val averageClosePrices =
      closePrices.head.cents.toDouble +: (closePrices zip closePrices.tail).map { case (l, r) => (l + r).cents.toDouble / smaPeriod }

    val smaCalculating = Source(prices)
      .via(calculating(SMAIndicator(smaPeriod)))
      .toList

    val sma = Await.result(smaCalculating, 3 seconds)

    sma should contain theSameElementsAs averageClosePrices
  }

}
