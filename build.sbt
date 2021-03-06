// General

ThisBuild / organization      := "com.streese.registravka4s"
ThisBuild / scalaVersion      := "2.13.5"
ThisBuild / version           := "0.0.0-SNAPSHOT"
ThisBuild / resolvers        ++= Seq("Confluent" at "https://packages.confluent.io/maven/")

Global / excludeLintKeys ++= Set(pomIncludeRepository, publishMavenStyle)

// Project Definitions

lazy val root = (project in file("."))
  .aggregate(core, akka, kafka, streams, benchmarks, docs, examples)
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

lazy val kafka = (project in file("kafka"))
  .settings(
    name                 := "registravka4s-kafka",
    libraryDependencies ++= Seq(libKafkaStreams)
  )
  .dependsOn(core)

lazy val streams = (project in file("streams"))
  .settings(
    name                 := "registravka4s-streams",
    libraryDependencies ++= Seq(libKafkaStreams)
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
    publish / skip       := true,
    buildInfoKeys        := Seq[BuildInfoKey](version),
    buildInfoPackage     := "com.streese.registravka4s.build.info"
  )
  .dependsOn(core, akka, kafka, streams)
  .enablePlugins(BuildInfoPlugin, MdocPlugin)

lazy val examples = (project in file("examples"))
  .settings(
    name           := "registravka4s-examples",
    publish / skip := true
  )
  .dependsOn(core, akka, kafka, streams)

// Dependencies

lazy val libAkkaStreamsKafka       = "com.typesafe.akka"     %% "akka-stream-kafka"        % "2.1.0"
lazy val libAvro4s                 = "com.sksamuel.avro4s"   %% "avro4s-core"              % "4.0.10"
lazy val libCoursier               = "io.get-coursier"       %% "coursier"                 % "2.0.16"
lazy val libKafka                  = "org.apache.kafka"      %% "kafka"                    % "2.8.0"
lazy val libKafkaStreams           = "org.apache.kafka"      %% "kafka-streams-scala"      % "2.8.0"
lazy val libKafkaStreamsAvroSerde  = "io.confluent"          %  "kafka-streams-avro-serde" % "6.2.0"
lazy val libPureConfig             = "com.github.pureconfig" %% "pureconfig"               % "0.16.0"

// Specifics For Publishing

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
ThisBuild / publishTo            := sonatypePublishToBundle.value
ThisBuild / publishMavenStyle    := true

ThisBuild / credentials += Credentials(
  realm    = "Sonatype Nexus Repository Manager",
  host     = "oss.sonatype.org",
  userName = sys.env.getOrElse("OSSRH_USER", ""),
  passwd   = sys.env.getOrElse("OSSRH_PASSWORD", "")
)
