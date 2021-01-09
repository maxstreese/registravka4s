// General Stuff

ThisBuild / organization      := "com.streese.registravka4s"
ThisBuild / scalaVersion      := "2.13.4"
ThisBuild / version           := "0.0.0-SNAPSHOT"
ThisBuild / resolvers        ++= Seq("Confluent" at "https://packages.confluent.io/maven/")

// Project Definitions

lazy val root = (project in file("."))
  .aggregate(core, akka, benchmarks, docs, examples)
  .settings(
    name           := "registravka4s",
    publish / skip := true
  )

lazy val core = (project in file("core"))
 .settings(
    name                 := "registravka4s-core",
    libraryDependencies ++= Seq(libAvro4s, libKafka, libKafkaStreamsAvroSerde, libPureConfig)
  )

lazy val akka = (project in file("akka"))
  .settings(
    name                 := "registravka4s-akka",
    libraryDependencies ++= Seq(libAkkaStreamsKafka)
  )
  .dependsOn(core)

lazy val benchmarks = (project in file("benchmarks"))
  .settings(
    name           := "registravka4s-benchmarks",
    publish / skip := true
  )
  .dependsOn(core)
  .enablePlugins(JmhPlugin)

lazy val docs = (project in file("mdoc"))
  .settings(
    libraryDependencies ++= Seq(libCoursier),
    mdoc                 := run.in(Compile).evaluated,
    mdocOut              := (ThisBuild / baseDirectory).value,
    publish / skip       := true
  )
  .dependsOn(core, akka)
  .enablePlugins(MdocPlugin)

lazy val examples = (project in file("examples"))
  .settings(
    name           := "registravka4s-examples",
    publish / skip := true
  )
  .dependsOn(core, akka)

// Dependencies

lazy val libAkkaStreamsKafka       = "com.typesafe.akka"     %% "akka-stream-kafka"        % "2.0.6"
lazy val libAvro4s                 = "com.sksamuel.avro4s"   %% "avro4s-core"              % "4.0.4"
lazy val libCoursier               = "io.get-coursier"       %% "coursier"                 % "2.0.8"
lazy val libKafka                  = "org.apache.kafka"      %% "kafka"                    % "2.7.0"
lazy val libKafkaStreamsAvroSerde  = "io.confluent"          %  "kafka-streams-avro-serde" % "6.0.1"
lazy val libPureConfig             = "com.github.pureconfig" %% "pureconfig"               % "0.14.0"

// Publishing

ThisBuild / organizationName     := "Streese"
ThisBuild / organizationHomepage := Some(url("https://streese.com/"))
ThisBuild / description          := "RegistrAvKa4s - (Schema-)Registry, Avro and Kafka for Scala"
ThisBuild / licenses             := List("Apache License 2.0" -> new URL("https://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / homepage             := Some(url("https://github.com/maxstreese/registravka4s"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    browseUrl  = url("https://github.com/maxstreese/registravka4s"),
    connection = "scm:git@github.com:maxstreese/registravka4s.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id    = "max.streese",
    name  = "Max Streese",
    email = "max@streese.com",
    url   = url("https://max.streese.com/")
  )
)

ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true
