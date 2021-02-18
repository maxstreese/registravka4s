package com.streese.registravka4s.kafka

import java.util.Properties

import com.streese.registravka4s._
import org.apache.kafka.clients.producer.{KafkaProducer => JKafkaProducer}

import scala.jdk.CollectionConverters._

object KafkaProducer {

  def apply[K : KeySerde, V : ValueSerde](configs: (String, AnyRef)*): JKafkaProducer[K, V] =
    KafkaProducer(Map(configs: _*))

  def apply[K : KeySerde, V : ValueSerde](configs: Map[String, AnyRef]): JKafkaProducer[K, V] =
    new JKafkaProducer(configs.asJava, KeySerializer[K], ValueSerializer[V])

  def apply[K : KeySerde, V : ValueSerde](properties: Properties): JKafkaProducer[K, V] =
    new JKafkaProducer(properties, KeySerializer[K], ValueSerializer[V])

}
