package com.streese.registravka4s

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.confluent.kafka.serializers.{AbstractKafkaSchemaSerDeConfig => SerDeConf}
import pureconfig.ConfigSource
import pureconfig.generic.auto._

import scala.util.Try

case class AvroSerdeConfig(schemaRegistryUrls: Seq[String], useMockedClient: Boolean = false) {
  def toMap = Map[String, Any](
    SerDeConf.SCHEMA_REGISTRY_URL_CONFIG -> schemaRegistryUrls.mkString(",")
  )
}

object AvroSerdeConfig {

  val configPath: String = "registravka4s"

  def load(config: Config): Try[AvroSerdeConfig] =
    Try(ConfigSource.fromConfig(config).loadOrThrow[AvroSerdeConfig])

  def load(configPath: String = configPath): Try[AvroSerdeConfig] = for {
    config <- Try(ConfigFactory.load().getConfig(configPath))
    result <- load(config)
  } yield result

}
