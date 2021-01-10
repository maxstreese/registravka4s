package com.streese.registravka4s.mdoc

import coursier._

object Main {

  def main(args: Array[String]): Unit = {

    val module = mod"com.streese.registravka4s::registravka4s-core"

    val res = Resolve()
    .addDependencies(Dependency(module, "latest.release"))
    .run()

    val version = res.reconciledVersions.get(module).get

    val settings = mdoc.MainSettings()
      .withSiteVariables(Map("VERSION" -> version))
      .withArgs(args.toList)

    val exitCode = mdoc.Main.process(settings)

    if (exitCode != 0) sys.exit(exitCode)

  }

}
