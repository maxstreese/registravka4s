package com.streese.registravka4s.benchmarks

import com.sksamuel.avro4s.{AvroSchema, DefaultFieldMapper, Encoder, ImmutableRecord}
import com.streese.registravka4s.benchmarks.Avro4sBenchmarkSetup._
import org.openjdk.jmh.annotations.{Benchmark, Scope, State}

object JMHSample_02_Avro4s {

  @State(Scope.Benchmark)
  class BenchmarkState {

      private val county = County("Bucks", Seq(Town("Hardwick", 123), Town("Weedon", 225)), true, 12.34, 0.123)

      val immutableRecord = Encoder[County].encode(county).asInstanceOf[ImmutableRecord]

  }

}

class JMHSample_02_Avro4s {

  import JMHSample_02_Avro4s._

  @Benchmark
  def specific(state: BenchmarkState): Unit = emulateEncodingForSpecificRecord(state.immutableRecord)

  @Benchmark
  def generic(state: BenchmarkState): Unit = emulateEncodingForGenericRecord(state.immutableRecord)

}
