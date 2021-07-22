# RegistrAvKa4s

[<img src="https://img.shields.io/github/workflow/status/maxstreese/registravka4s/Build/main?style=flat-square&logo=github"/>](https://github.com/maxstreese/registravka4s/actions?query=workflow%3ABuild+branch%3Amain)
[<img src="https://img.shields.io/maven-central/v/com.streese.registravka4s/registravka4s-core_2.13?style=flat-square&logo=apache%20maven"/>](https://search.maven.org/search?q=g:com.streese.registravka4s)

RegistrAvKa4s allows you to produce/consume Confluent Schema Registry compatible Apache Avro records to/from
Apache Kafka by simply defining your data model as ADTs (via traits and case classes) in plain Scala code.

## Motivation

Kafka itself does not care which encoding you use for the data you put into it so your options are basically limitless.
If however schema evolution is a concern for you and you want better integration with the broader Kafka ecosystem then
utilizing the Confluent Schema Registry is a good choice.

When using Schema Registry your choice is between Avro, JSON and Protocol Buffers. If performance is of concern then
non-binary formats like JSON should be ruled out. Therefore Avro and Protocol Buffers remain. Between those two the
choice is less clear to me. This library focuses on Avro simply because it was the original format supported by
Schema Registry and to my knowledge provides the best integration with other ecosystem components (though I could
be proven wrong here).

Now that we know we would like to use Schema Registry with the Avro format and code in Scala, what's next?
There are two options I know of:

1. Write Avro schemata by hand and either automatically or manually generate codecs for them in a way that integrates with Schema Registry
2. Write Scala ADTs (via traits and case classes) and have some magic wire things up for you such that schemata and codecs are derived and integrated with Schema Registry

As far as I know option one is the one you usually read about and also experience in other languages. I find this
method to be very tedious and inelegant. It is more labour intensive than option two and requires more code/scaffolding.

In comparison when going with option two all you got to do is define your data model as you would anyway in Scala
via traits and case classes. An approach I consider to be much more elegant and also more comprehensible. It is this
option that RegistrAvKa4s strives to provide.

Finally two notes about all of this:

* Extending this library to work with JSON and Protocol Buffers as well might be a nice addition
* The thoughts above represent my own thinking. If someone has points to add to the above or disprove it please feel free to share them

## Dependencies For A Project In SBT Style

```scala
lazy val registravka4sVersion = "@VERSION@"

libraryDependencies ++= (
  "com.streese.registravka4s" %% "registravka4s-akka"    % registravka4sVersion,
  "com.streese.registravka4s" %% "registravka4s-kafka"   % registravka4sVersion,
  "com.streese.registravka4s" %% "registravka4s-streams" % registravka4sVersion
)
```

Please note that of all the libraries listed above you usually only require one - the one which wires up the
library/framework you use to interact with Kafka.

## Examples

All of the examples below can also be found in application form as part of the `examples` sub-project in this
repository.

### Akka Streams Producer

Suppose you want to produce some records into a Kafka topic that represent some financial market data (referred to as
ticks in this example). With RegistrAvKa4s all you need to do is define your data model as plain Scala case classes.
The derivation of all Avro schemata and records as well as integration with the Confluent Schema Registry is handled
for you.

```scala mdoc:compile-only
import java.time.Instant

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import com.streese.registravka4s.akka.ProducerSettings
import com.streese.registravka4s.AvroSerdeConfig
import com.streese.registravka4s.GenericRecordFormat.Implicits._
import com.streese.registravka4s.GenericSerde.Implicits._
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

### Kafka Producer

You may want to do something like the above but with the core Kafka client library instead. This may for example be
useful in some Ammonite scripting context where you do not want to concern yourself with e.g. running an actor system
like above.

```scala mdoc:compile-only
import java.time.Instant

import com.streese.registravka4s.GenericRecordFormat.Implicits._
import com.streese.registravka4s.GenericSerde.Implicits._
import com.streese.registravka4s.streams.ImplicitConversions._
import com.streese.registravka4s.kafka.KafkaProducer
import com.streese.registravka4s.{AvroSerdeConfig, GenericRecordFormat, GenericSerde}
import org.apache.kafka.clients.producer.ProducerRecord

case class Instrument(isin: String, currency: String)
case class Tick(instrument: Instrument, timestamp: Instant, price: Double)

implicit val avroSerdeConfig: AvroSerdeConfig = AvroSerdeConfig(Seq("http://localhost:8081"))

val topic = "ticks"
val instrument = Instrument("DE0008469008", "PTX")

val producer = KafkaProducer[Instrument, Tick]("bootstrap.servers" -> "localhost:9092")
producer.send(new ProducerRecord(topic, instrument, Tick(instrument, Instant.now(), 1.0)))
producer.close()
```

### Kafka Streams Processor

Suppose you want to join some ticks with reference data via a KStream-KTable-Join. As before simply define your data
model and processor topology in Scala and Avro is handled for you under the hood.

```scala mdoc:compile-only
import java.time.Instant
import java.util.Properties

import com.streese.registravka4s.GenericRecordFormat.Implicits._
import com.streese.registravka4s.GenericSerde.Implicits._
import com.streese.registravka4s.streams.ImplicitConversions._
import com.streese.registravka4s.{AvroSerdeConfig, GenericRecordFormat, GenericSerde}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{KStream, KTable}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

case class Instrument(isin: String, currency: String)
case class Tick(instrument: Instrument, timestamp: Instant, price: Double)
case class RefData(instrument: Instrument, name: String, `type`: String)
case class TickWithRefData(tick: Tick, refData: RefData)

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
```

## Special Credits

If you look at the amount of code in this repository you will realize that it is actually very little. In the end all
that is really done here is wiring up a few existing libraries in what I would consider a fairly smart way.
Therefore special credit goes to:

* [Avro4s](https://github.com/sksamuel/avro4s): The library that derives Avro schemata and codecs for your Scala ADTs
* [@yeryomenkom](https://github.com/yeryomenkom): The smart guy I know that actually came up with most of this code
