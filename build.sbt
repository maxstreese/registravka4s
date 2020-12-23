ThisBuild / organization  := "com.streese"
ThisBuild / version       := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion  := "2.13.4"
ThisBuild / resolvers    ++= Seq("Confluent" at "https://packages.confluent.io/maven/")

lazy val core = (project in file("core"))
  .settings(
    name                 := "registravka4s-core",
    libraryDependencies ++= Deps.avro4s ++ Deps.kafka ++ Deps.kafkaAvro ++ Deps.pureConfig
  )

lazy val akka = (project in file("akka"))
  .dependsOn(core)
  .settings(
    name := "registravka4s-akka",
    libraryDependencies ++= Deps.akka
  )

lazy val docs = (project in file("mdoc"))
  .settings(
    mdocOut := (ThisBuild / baseDirectory).value,
    mdocVariables := Map(
      "VERSION" -> version.value
    )
  )
  .dependsOn(core, akka)
  .enablePlugins(MdocPlugin)
