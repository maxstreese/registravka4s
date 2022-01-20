package com.streese.registravka4s.benchmarks

import com.streese.registravka4s.AvroSerdeConfig
import com.streese.registravka4s.GenericRecordFormat
import com.streese.registravka4s.GenericSerde
import com.streese.registravka4s.WrappedGenericRecord
import com.streese.registravka4s.benchmarks.model._
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State

object JMHSample_01_GenericRecordSerialization {

  @State(Scope.Benchmark)
  class BenchmarkState {

    private val county = County("Bucks", Seq(Town("Hardwick", 123), Town("Weedon", 225)), true, 12.34, 0.123)

    val genericAvroSerde = GenericSerde.Implicits.genericAvroSerde(AvroSerdeConfig(Seq("localhost:8081"), true), false)

    val record = GenericRecordFormat.Implicits.avroRecordFormatGeneric[County].to(county)

    val topic = "people"

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
