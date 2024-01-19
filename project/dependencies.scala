import sbt._
import Keys._

object dependencies {

  private object versions {
    val akkaStreamsKafka      = "4.0.0"
    val avro4s                = "4.1.0"
    val kafka                 = "3.3.1"
    val kafkaStreams          = "3.3.1"
    val kafkaStreamsAvroSerde = "7.3.1"
    val logback               = "1.4.6"
    val pureConfig            = "0.17.2"
    val scalaLogging          = "3.9.5"
    val testcontainersScala   = "0.40.12"
  }

  private object libs {
    val pureconfig              = "com.github.pureconfig"      %% "pureconfig"                % versions.pureConfig
    val avro4sCore              = "com.sksamuel.avro4s"        %% "avro4s-core"               % versions.avro4s
    val kafkaStreamsAvroSerde   = "io.confluent"                % "kafka-streams-avro-serde"  % versions.kafkaStreamsAvroSerde
    val kafka                   = "org.apache.kafka"           %% "kafka"                     % versions.kafka
    val logbackClassic          = "ch.qos.logback"              % "logback-classic"           % versions.logback
    val akkaStreamsKafka        = "com.typesafe.akka"          %% "akka-stream-kafka"         % versions.akkaStreamsKafka
    val kafkaStreamsScala       = "org.apache.kafka"           %% "kafka-streams-scala"       % versions.kafkaStreams
    val scalaLogging            = "com.typesafe.scala-logging" %% "scala-logging"             % versions.scalaLogging
    val testcontainersScalaCore = "com.dimafeng"               %% "testcontainers-scala-core" % versions.testcontainersScala
  }

  val coreDeps: Seq[ModuleID] = Seq(
    libs.pureconfig,
    libs.avro4sCore,
    libs.kafkaStreamsAvroSerde,
    libs.kafka,
  )

  val akkaDeps: Seq[ModuleID] = Seq(
    libs.akkaStreamsKafka,
  )

  val streamsDeps: Seq[ModuleID] = Seq(
    libs.kafkaStreamsScala,
  )

  val examplesDeps: Seq[ModuleID] = Seq(
    libs.logbackClassic,
    libs.scalaLogging,
    libs.testcontainersScalaCore,
  )

}
