ThisBuild / organization  := "com.streese"
ThisBuild / scalaVersion  := "2.13.4"
ThisBuild / resolvers    ++= Seq("Confluent" at "https://packages.confluent.io/maven/")

lazy val core = (project in file("core"))
 .settings(
    name                 := "registravka4s-core",
    libraryDependencies ++= Deps.avro4s ++ Deps.kafka ++ Deps.kafkaAvro ++ Deps.pureConfig,
  )
  .enablePlugins(GitVersioning)

lazy val akka = (project in file("akka"))
  .settings(
    name := "registravka4s-akka",
    libraryDependencies ++= Deps.akka
  )
  .dependsOn(core)
  .enablePlugins(GitVersioning)

lazy val benchmarks = (project in file("benchmarks"))
  .settings(
    name := "registravka4s-benchmarks"
  )
  .dependsOn(core)
  .enablePlugins(GitVersioning, JmhPlugin)

lazy val docs = (project in file("mdoc"))
  .settings(
    mdocOut := (ThisBuild / baseDirectory).value,
    mdocVariables := Map(
      "VERSION" -> version.value
    )
  )
  .dependsOn(core, akka)
  .enablePlugins(GitVersioning, MdocPlugin)

lazy val examples = (project in file("examples"))
  .settings(
    name := "registravka4s-examples"
  )
  .dependsOn(core, akka)
  .enablePlugins(GitVersioning)
