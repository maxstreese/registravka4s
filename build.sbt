ThisBuild / organization  := "com.streese"
ThisBuild / scalaVersion  := "2.13.4"
ThisBuild / resolvers    ++= Seq("Confluent" at "https://packages.confluent.io/maven/")

enablePlugins(GitVersioning)
ThisBuild / git.useGitDescribe        := true
ThisBuild / git.gitTagToVersionNumber := { tag: String =>
  if(tag matches "[0-9]+\\.[0-9]+\\.[0-9]+") Some(tag)
  else None
}

lazy val libAkkaStreamsKafka       = "com.typesafe.akka"     %% "akka-stream-kafka"        % "2.0.6"
lazy val libAvro4s                 = "com.sksamuel.avro4s"   %% "avro4s-core"              % "4.0.3"
lazy val libCoursier               = "io.get-coursier"       %% "coursier"                 % "2.0.7"
lazy val libKafka                  = "org.apache.kafka"      %% "kafka"                    % "2.6.0"
lazy val libKafkaStreamsAvroSerde  = "io.confluent"          %  "kafka-streams-avro-serde" % "6.0.1"
lazy val libPureConfig             = "com.github.pureconfig" %% "pureconfig"               % "0.14.0"

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
    name := "registravka4s-benchmarks"
  )
  .dependsOn(core)
  .enablePlugins(JmhPlugin)

lazy val docs = (project in file("mdoc"))
  .settings(
    libraryDependencies ++= Seq(libCoursier),
    mdoc                 := run.in(Compile).evaluated,
    mdocOut              := (ThisBuild / baseDirectory).value
  )
  .dependsOn(core, akka)
  .enablePlugins(MdocPlugin)

lazy val examples = (project in file("examples"))
  .settings(
    name := "registravka4s-examples"
  )
  .dependsOn(core, akka)
