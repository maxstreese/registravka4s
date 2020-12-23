import sbt._
import Keys._

object Deps {

  val akka       = Seq("com.typesafe.akka" %% "akka-stream-kafka" % "2.0.6")
  val avro4s     = Seq("com.sksamuel.avro4s" %% "avro4s-core" % "4.0.3")
  val kafka      = Seq("org.apache.kafka" %% "kafka" % "2.6.0")
  val kafkaAvro  = Seq("io.confluent" % "kafka-streams-avro-serde" % "6.0.1")
  val pureConfig = Seq("com.github.pureconfig" %% "pureconfig" % "0.14.0")

}
