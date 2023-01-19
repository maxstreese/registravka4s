import sbt._
import Keys._

object dependencies {

  private object versions {
    val akkaStreamsKafka      = "4.0.0"
    val avro4s                = "4.1.0"
    val kafka                 = "3.3.1"
    val kafkaStreams          = "3.3.1"
    val kafkaStreamsAvroSerde = "7.3.1"
    val pureConfig            = "0.17.2"
  }

  val coreDeps: Seq[ModuleID] = Seq(
    "com.github.pureconfig" %% "pureconfig"               % versions.pureConfig,
    "com.sksamuel.avro4s"   %% "avro4s-core"              % versions.avro4s,
    "io.confluent"           % "kafka-streams-avro-serde" % versions.kafkaStreamsAvroSerde,
    "org.apache.kafka"      %% "kafka"                    % versions.kafka
  )

  val akkaDeps: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-stream-kafka" % versions.akkaStreamsKafka
  )

  val streamsDeps: Seq[ModuleID] = Seq(
    "org.apache.kafka" %% "kafka-streams-scala" % versions.kafkaStreams
  )

}
