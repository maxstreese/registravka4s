package com.streese.registravka4s.benchmarks

import com.streese.registravka4s.AvroSerdeConfig
import com.streese.registravka4s.Serdes.Implicits
import com.streese.registravka4s.benchmarks.Records._
import org.openjdk.jmh.annotations.{Benchmark, Scope, State}
import com.streese.registravka4s.benchmarks.Avro4sBenchmarkSetup.{County, Town}

object JMHSample_01_GenericRecordSerialization {

  @State(Scope.Benchmark)
  class BenchmarkState {

    val topic = "people"

    val county = County("Bucks", Seq(Town("Hardwick", 123), Town("Weedon", 225)), true, 12.34, 0.123)

    val genericAvroSerde =
      Implicits.genericAvroSerde(AvroSerdeConfig(Seq("localhost:8081"), useMockedClient = true), forKey = false)

  }

}

class JMHSample_01_GenericRecordSerialization {

  import JMHSample_01_GenericRecordSerialization._

  @Benchmark
  def unwrapped(state: BenchmarkState): Unit = {
    state.genericAvroSerde.serializer().serialize(
      state.topic,
      Implicits.avroRecordFormatGeneric[County].to(state.county)
    )
  }

  @Benchmark
  def wrapped(state: BenchmarkState): Unit = {
    state.genericAvroSerde.serializer().serialize(
      state.topic,
      new WrappedGenericRecord(Implicits.avroRecordFormatGeneric[County].to(state.county))
    )
  }

}
