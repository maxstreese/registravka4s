package com.streese.registravka4s.kafka

import com.streese.registravka4s._
import org.apache.kafka.clients.consumer.{KafkaConsumer => JKafkaConsumer}

import java.util.Properties
import scala.jdk.CollectionConverters._

object KafkaConsumer {

  def apply[K : KeySerde, V : ValueSerde](configs: (String, AnyRef)*): JKafkaConsumer[K, V] =
    KafkaConsumer(Map(configs: _*))

  def apply[K : KeySerde, V : ValueSerde](properties: Properties): JKafkaConsumer[K, V] =
    new JKafkaConsumer(properties, KeyDeserializer[K], ValueDeserializer[V])

  def apply[K : KeySerde, V : ValueSerde](configs: Map[String, AnyRef]): JKafkaConsumer[K, V] =
    new JKafkaConsumer(configs.asJava, KeyDeserializer[K], ValueDeserializer[V])

}
