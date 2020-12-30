# RegistrAvKa4s

RegistrAvKa4s allows you to produce/consume Confluent Schema Registry compatible Apache Avro records to/from
Apache Kafka by simply defining your data model as ADTs (via traits and case classes) in plain Scala code.

## Motivation

t.b.d.

## Dependencies For A Project In SBT Style

Please note that the only required dependency for any project from the below list is `core`. All other dependencies
may be added depending on the library/framework you use to interact with Kafka (e.g. `akka`). As of writing this,
Registravka4s does only support Akka.

```scala
libraryDependencies ++= (
  "com.streese" % "registravka4s-core" % "@VERSION@",
  "com.streese" % "registravka4s-akka" % "@VERSION@"
)
```

## Complete Example For An Akka Streams Kafka Producer

Suppose you want to produce some records into a Kafka topic that represent some financial market data (referred to as
ticks in this example). With Registravka4s all you need to do is define your data model as plain Scala case classes.
The derivation of all Avro schemata and records as well as integration with the Confluent Schema Registry is handled
for you.

```scala mdoc:compile-only
import java.time.Instant

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import com.streese.registravka4s.akka.ProducerSettings
import com.streese.registravka4s.AvroSerdeConfig
import com.streese.registravka4s.Serdes.Implicits._
import org.apache.kafka.clients.producer.ProducerRecord

case class Instrument(isin: String, currency: String)
case class Tick(instrument: Instrument, timestamp: Instant, price: Double)

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
```

This example is also available in application form as part of the `examples` sub-module in this repository. Assuming
your execution environment meets all requirements (compatibile JDK and SBT versions installed) and you got Kafka
running and exposed at `localhost:9092` as well as Schema Registry at `localhost:8081` simply execute
`sbt examples/run`.

## Special Credits

t.b.d.
