package com.streese.registravka4s.benchmarks

import com.streese.registravka4s.AvroSerdeConfig
import com.streese.registravka4s.Serdes.Implicits
import com.streese.registravka4s.benchmarks.models._
import org.openjdk.jmh.annotations.{Benchmark, Scope, State}

object JMHSample_01_GenericRecordSerialization {

  @State(Scope.Benchmark)
  class BenchmarkState {

    private val county = County("Bucks", Seq(Town("Hardwick", 123), Town("Weedon", 225)), true, 12.34, 0.123)

    val record = Implicits.avroRecordFormatGeneric[County].to(county)

    val topic = "people"

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
      state.record
    )
  }

  @Benchmark
  def wrapped(state: BenchmarkState): Unit = {
    state.genericAvroSerde.serializer().serialize(
      state.topic,
      new WrappedGenericRecord(state.record)
    )
  }

}
