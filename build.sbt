ThisBuild / organization  := "com.streese"
ThisBuild / scalaVersion  := "2.13.4"
ThisBuild / resolvers    ++= Seq("Confluent" at "https://packages.confluent.io/maven/")

enablePlugins(GitVersioning)
ThisBuild / git.useGitDescribe        := true
ThisBuild / git.gitTagToVersionNumber := { tag: String =>
  if(tag matches "[0-9]+\\..*") Some(tag)
  else None
}

lazy val core = (project in file("core"))
 .settings(
    name                 := "registravka4s-core",
    libraryDependencies ++= Deps.avro4s ++ Deps.kafka ++ Deps.kafkaAvro ++ Deps.pureConfig
  )

lazy val akka = (project in file("akka"))
  .settings(
    name := "registravka4s-akka",
    libraryDependencies ++= Deps.akka
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
    mdocOut := (ThisBuild / baseDirectory).value,
    mdocVariables := Map(
      "VERSION" -> version.value
    )
  )
  .dependsOn(core, akka)
  .enablePlugins(MdocPlugin)

lazy val examples = (project in file("examples"))
  .settings(
    name := "registravka4s-examples"
  )
  .dependsOn(core, akka)
