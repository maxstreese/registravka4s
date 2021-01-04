package com.streese.registravka4s.mdoc

import coursier._

object Main {

  def main(args: Array[String]): Unit = {

    // TODO: Replace this with registravka4s-core once a version of it is actually published
    val module = mod"com.sksamuel.avro4s::avro4s-core"

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
