package com.streese.registravka4s.examples

import java.time.Instant

object model {

  case class Instrument(isin: String, currency: String)

  case class Tick(instrument: Instrument, timestamp: Instant, price: Double)

  case class RefData(instrument: Instrument, name: String, `type`: String)

  case class TickWithRefData(tick: Tick, refData: RefData)

}
