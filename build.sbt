ThisBuild / organization := "com.streese"
ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.4"

lazy val core = (project in file("core"))
  .settings {
    name                 := "registravka-core"
    libraryDependencies ++= Deps.avro4s ++ Deps.kafka
  }

lazy val akka = (project in file("akka"))
  .dependsOn(core)
  .settings {
    name := "registravka-akka"
  }

lazy val streams = (project in file("streams"))
  .dependsOn(core)
  .settings {
    name := "registravka-streams"
  }
