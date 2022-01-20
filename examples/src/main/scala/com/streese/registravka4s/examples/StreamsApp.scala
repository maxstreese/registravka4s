package com.streese.registravka4s.examples

import com.streese.registravka4s.AvroSerdeConfig
import com.streese.registravka4s.GenericRecordFormat
import com.streese.registravka4s.GenericSerde
import com.streese.registravka4s.examples.model._
import com.streese.registravka4s.streams.ImplicitConversions._
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.kafka.streams.scala.kstream.KTable

import java.util.Properties

object StreamsApp extends App with GenericRecordFormat with GenericSerde {

  implicit val avroSerdeConfig: AvroSerdeConfig = AvroSerdeConfig(Seq("http://localhost:8081"))

  val builder = new StreamsBuilder()

  val ticks: KStream[Instrument, Tick] = builder.stream("ticks")
  val refs: KTable[Instrument, RefData] = builder.table("refs")

  val ticksWithRef = ticks.join(refs)((tick, ref) => TickWithRefData(tick, ref))
  ticksWithRef.to("ticks-with-ref")

  val props = new Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "ticks-ref-joiner")
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")

  val streams = new KafkaStreams(builder.build(), props)
  sys.addShutdownHook {
    streams.close()
  }
  streams.start()

}
