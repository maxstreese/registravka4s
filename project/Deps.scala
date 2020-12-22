import sbt._
import Keys._

object Deps {

  val avro4s = Seq("com.sksamuel.avro4s" %% "avro4s-core" % "4.0.3")
  val kafka  = Seq("org.apache.kafka" %% "kafka" % "2.6.0")

}
