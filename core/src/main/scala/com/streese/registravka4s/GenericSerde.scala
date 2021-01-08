package com.streese.registravka4s

import com.sksamuel.avro4s.RecordFormat
import org.apache.kafka.common.serialization.{Serde => KSerde, Serdes => KSerdes, Serializer, Deserializer}
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient

import scala.jdk.CollectionConverters._

trait GenericSerde {

  implicit def keyAvroSerdeGeneric[T >: Null](
    implicit
    config: AvroSerdeConfig,
    recordFormat: RecordFormat[T]
  ): KeySerde[T] = kafkaSerde(genericAvroSerde(config, forKey = true), recordFormat).asInstanceOf[KeySerde[T]]

  implicit def valueAvroSerdeGeneric[T >: Null](
    implicit
    config: AvroSerdeConfig,
    recordFormat: RecordFormat[T]
  ): ValueSerde[T] = kafkaSerde(genericAvroSerde(config, forKey = false), recordFormat).asInstanceOf[ValueSerde[T]] 

  private[registravka4s] def kafkaSerde[T >: Null](
    genericAvroSerde: GenericAvroSerde,
    recordFormat: RecordFormat[T]
  ): KSerde[T] = {
    KSerdes.serdeFrom(
      new Serializer[T] {
        override def serialize(topic: String, data: T): Array[Byte] = {
          if (data == null) null
          else genericAvroSerde.serializer().serialize(topic, new WrappedGenericRecord(recordFormat.to(data)))
        }
      },
      new Deserializer[T] {
        override def deserialize(topic: String, data: Array[Byte]): T = {
          if (data == null) null
          else recordFormat.from(genericAvroSerde.deserializer().deserialize(topic, data))
        }
      }
    )
  }

  private[registravka4s] def genericAvroSerde(config: AvroSerdeConfig, forKey: Boolean): GenericAvroSerde = {
    val serde =
      if(config.useMockedClient) new GenericAvroSerde(new MockSchemaRegistryClient())
      else new GenericAvroSerde()
    serde.configure(config.toMap.asJava, forKey)
    serde
  }

}

object GenericSerde {

  object Implicits extends GenericSerde

}
