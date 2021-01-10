package com.streese.registravka4s.mdoc

import com.streese.registravka4s.build.info.BuildInfo
import coursier._

import scala.util.Try

object Main {

  def main(args: Array[String]): Unit = {

    val settings = mdoc.MainSettings()
      .withSiteVariables(Map("VERSION" -> determineVersion()))
      .withArgs(args.toList)

    val exitCode = mdoc.Main.process(settings)

    if (exitCode != 0) sys.exit(exitCode)

  }

  def determineVersion(): String = {
    if (BuildInfo.version.endsWith("-SNAPSHOT")) {
      Try {

        val module = mod"com.streese.registravka4s::registravka4s-core"

        val res = Resolve()
          .addDependencies(Dependency(module, "latest.release"))
          .run()

        res.reconciledVersions.get(module).get

      }.getOrElse(BuildInfo.version)
    }
    else BuildInfo.version
  }

}
