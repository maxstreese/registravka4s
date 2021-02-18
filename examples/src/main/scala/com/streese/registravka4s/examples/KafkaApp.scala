package com.streese.registravka4s.examples

import java.time.Instant

import com.streese.registravka4s.examples.model._
import com.streese.registravka4s.kafka.KafkaProducer
import com.streese.registravka4s.{AvroSerdeConfig, GenericRecordFormat, GenericSerde}
import org.apache.kafka.clients.producer.ProducerRecord

object KafkaApp extends App with GenericRecordFormat with GenericSerde {

  implicit val avroSerdeConfig: AvroSerdeConfig = AvroSerdeConfig(Seq("http://localhost:8081"))

  val topic = "ticks"
  val instrument = Instrument("DE0008469008", "PTX")

  val producer = KafkaProducer[Instrument, Tick]("bootstrap.servers" -> "localhost:9092")
  producer.send(new ProducerRecord(topic, instrument, Tick(instrument, Instant.now(), 1.0)))
  producer.close()

}
