package com.streese.registravka4s.examples

import java.time.Instant

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import com.streese.registravka4s.akka.ProducerSettings
import com.streese.registravka4s.examples.model._
import com.streese.registravka4s.{AvroSerdeConfig, GenericRecordFormat, GenericSerde}
import org.apache.kafka.clients.producer.ProducerRecord

object Main extends App with GenericRecordFormat with GenericSerde {

  implicit val actorSystem: ActorSystem = ActorSystem("registravka4s-example-actor-system")
  implicit val avroSerdeConfig: AvroSerdeConfig = AvroSerdeConfig(Seq("http://localhost:8081"))

  val producerSettings = ProducerSettings[Instrument, Tick](actorSystem)
    .withBootstrapServers("localhost:9092")

  val topic = "ticks"
  val instrument = Instrument("DE0008469008", "PTX")

  val done = Source(1 to 10)
    .map(i => instrument -> Tick(instrument, Instant.now(), i))
    .map { case (i, t) => new ProducerRecord[Instrument, Tick](topic, i, t) }
    .runWith(Producer.plainSink(producerSettings))

  implicit val executionContext = actorSystem.dispatcher
  done.onComplete(_ => actorSystem.terminate())

}
