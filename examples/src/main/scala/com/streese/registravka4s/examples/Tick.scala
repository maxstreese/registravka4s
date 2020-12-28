package com.streese.registravka4s.examples

import java.time.Instant

case class Tick(
  instrument: Instrument,
  timestamp:  Instant,
  price:      Double
)
