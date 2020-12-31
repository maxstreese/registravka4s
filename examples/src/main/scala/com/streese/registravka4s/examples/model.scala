package com.streese.registravka4s.examples

import java.time.Instant

object model {

  case class Instrument(isin: String, currency: String)

  case class Tick(instrument: Instrument, timestamp: Instant, price: Double)

}
