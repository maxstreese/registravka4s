package com.streese.registravka4s.benchmarks

object Records {

  case class SomeComplexRecord(
    string:         String,
    int:            Int,
    long:           Long,
    double:         Double,
    boolean:        Boolean,
    stringSequence: Seq[String],
    intSequence:    Seq[Int],
    longSequence:   Seq[Long],
    nested:         SomeLessComplexRecord,
    nestedSequence: Seq[SomeLessComplexRecord]
  )

  case class SomeLessComplexRecord(
    string:         String,
    int:            Int,
    long:           Long,
    double:         Double,
    boolean:        Boolean
  )

}
