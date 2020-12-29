package com.streese.registravka4s.benchmarks

import com.streese.registravka4s.AvroSerdeConfig
import com.streese.registravka4s.Serdes.Implicits
import com.streese.registravka4s.benchmarks.Records._
import org.openjdk.jmh.annotations.{Benchmark, Scope}

import scala.util.Try
import org.openjdk.jmh.annotations.State

object JMHSample_01_GenericRecordSerialization {

  @State(Scope.Benchmark)
  class BenchmarkState {

    val topic = "people"

    private val lessComplexRecord = SomeLessComplexRecord(
      string         = "42",
      int            = 42,
      long           = 42,
      double         = 42.0,
      boolean        = true
    )

    val record = SomeComplexRecord(
      string         = "42",
      int            = 42,
      long           = 42,
      double         = 42.0,
      boolean        = true,
      stringSequence = (1 to 10).map(_ => "42"),
      intSequence    = (1 to 10).map(_ => 42),
      longSequence   = (1 to 10).map(_ => 42),
      nested         = lessComplexRecord,
      nestedSequence = (1 to 10).map(_ => lessComplexRecord)
    )

    val genericAvroSerde =
      Implicits.genericAvroSerde(AvroSerdeConfig(Seq("localhost:8081"), useMockedClient = true), forKey = false)

  }

}

class JMHSample_01_GenericRecordSerialization {

  import JMHSample_01_GenericRecordSerialization._

  @Benchmark
  def baseline(state: BenchmarkState): Unit = (
  )

  @Benchmark
  def unwrapped(state: BenchmarkState): Unit = {
    state.genericAvroSerde.serializer().serialize(
      state.topic,
      Implicits.avroRecordFormatGeneric[SomeComplexRecord].to(state.record)
    )
  }

  @Benchmark
  def wrapped(state: BenchmarkState): Unit = {
    state.genericAvroSerde.serializer().serialize(
      state.topic,
      new WrappedGenericRecord(Implicits.avroRecordFormatGeneric[SomeComplexRecord].to(state.record))
    )
  }

}
