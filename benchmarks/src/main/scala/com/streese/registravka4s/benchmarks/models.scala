package com.streese.registravka4s.benchmarks

object models {

  case class County(name: String, towns: Seq[Town], ceremonial: Boolean, lat: Double, long: Double)
  case class Town(name: String, population: Int)

}
