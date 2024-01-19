package com.streese.registravka4s.examples

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.Subscriptions
import akka.kafka.scaladsl.Consumer
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import com.dimafeng.testcontainers.DockerComposeContainer
import com.dimafeng.testcontainers.ExposedService
import com.sksamuel.avro4s.AvroName
import com.streese.registravka4s.AvroSerdeConfig
import com.streese.registravka4s.GenericRecordFormat
import com.streese.registravka4s.GenericSerde
import com.streese.registravka4s.PrimitiveSerdes
import com.streese.registravka4s.akka.ConsumerSettings
import com.streese.registravka4s.akka.ProducerSettings
import com.typesafe.scalalogging.StrictLogging
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.testcontainers.containers.wait.strategy.Wait

import java.io.File
import java.time.Instant
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

object SchemaChangeApp extends App with GenericRecordFormat with GenericSerde with PrimitiveSerdes with StrictLogging {

  object names {
    val broker         = "broker"
    val schemaRegistry = "schema-registry"
    val kafkaUi        = "kafka-ui"
  }

  object readyLogs {
    val broker         = """.*\[KafkaServer id=[0-9]+\] started \(kafka\.server\.KafkaServer\).*"""
    val schemaRegistry = """.*Server started, listening for requests.*"""
    val kafkaUi        = """.*Netty started on port 8080.*"""
  }

  val compose =
    DockerComposeContainer
      .Def(
        composeFiles        = new File("src/main/resources/docker-compose-schema-change.yaml"),
        tailChildContainers = false,
        exposedServices     = Seq(
          ExposedService(names.broker,         9092, Wait.forLogMessage(readyLogs.broker, 1)),
          ExposedService(names.schemaRegistry, 8081, Wait.forLogMessage(readyLogs.schemaRegistry, 1)),
          ExposedService(names.kafkaUi,        8080, Wait.forLogMessage(readyLogs.kafkaUi, 1)),
        )
      )
      .start()

  @AvroName("Versioned")
  case class VersionOne(ts: Instant, a: Int)

  @AvroName("Versioned")
  case class VersionTwo(ts: Instant, a: Int, b: Int = 0)

  implicit val actorSystem: ActorSystem = ActorSystem("schema-change-app")
  implicit val avroSerdeConfig: AvroSerdeConfig = AvroSerdeConfig(Seq("http://localhost:8081"))

  val server = "localhost:9092"
  val topic  = "versioned_data"

  val producerSettingsOne =
    ProducerSettings[String, VersionOne](actorSystem)
      .withBootstrapServers(server)

  def producerOneDone(): Future[Done] =
    Source(1 to 10)
      .map(i => i.toString -> VersionOne(Instant.now(), i))
      .map { case (i, v) => new ProducerRecord[String, VersionOne](topic, i, v) }
      .runWith(Producer.plainSink(producerSettingsOne))

  val producerSettingsTwo =
    ProducerSettings[String, VersionTwo](actorSystem)
      .withBootstrapServers(server)

  def producerTwoDone(): Future[Done] =
    Source(11 to 20)
      .map(i => i.toString -> VersionTwo(Instant.now(), i, i))
      .map { case (i, v) => new ProducerRecord[String, VersionTwo](topic, i, v) }
      .runWith(Producer.plainSink(producerSettingsTwo))

  val consumerSettings =
    ConsumerSettings[String, VersionTwo](actorSystem)
      .withBootstrapServers(server)
      .withGroupId("schema-change-app")
      .withProperties(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "earliest")

  def consumerDone(): Future[Done] =
    Consumer
      .plainSource(consumerSettings, Subscriptions.topics(topic))
      .map(r => r.value)
      .runForeach { r =>
        logger.info(s"received record value: $r")
      }

  val done = for {
    _ <- producerOneDone()
    _ <- producerTwoDone()
    _ <- consumerDone()
  } yield ()

  done.onComplete {
    case Success(_) => logger.error("completed with success")
    case Failure(e) => logger.error("completed with failure", e)
  }

}
