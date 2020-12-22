package com.streese.registravka4s

import io.confluent.kafka.serializers.{AbstractKafkaSchemaSerDeConfig => SerDeConf}

case class Config(
  schemaRegistryUrls: Seq[String]
) {
  def toMap = Map[String, Any](
    SerDeConf.SCHEMA_REGISTRY_URL_CONFIG -> schemaRegistryUrls.mkString(",")
  )
}
