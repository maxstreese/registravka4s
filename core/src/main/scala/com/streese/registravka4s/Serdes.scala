package com.streese.registravka4s

import java.lang.{Long => JLong, Float => JFloat, Double => JDouble, Integer => JInt}
import java.{util => ju}

import com.sksamuel.avro4s.RecordFormat
import org.apache.kafka.common.utils.{Bytes => KBytes}
import org.apache.kafka.common.serialization.{Serde => KSerde, Serdes => KSerdes, Serializer, Deserializer}
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde

import scala.jdk.CollectionConverters._

object Serdes {
  object Implicits extends Serdes
}

trait Serdes {

  implicit val keyAvroSerdeString: KeySerde[String] = KSerdes.String().asInstanceOf[KeySerde[String]]
  implicit val keyAvroSerdeLong: KeySerde[Long] = KSerdes.Long().asInstanceOf[KeySerde[Long]]
  implicit val keyAvroSerdeJavaLong: KeySerde[JLong] = KSerdes.Long().asInstanceOf[KeySerde[JLong]]
  implicit val keyAvroSerdeByteArray: KeySerde[Array[Byte]] = KSerdes.ByteArray().asInstanceOf[KeySerde[Array[Byte]]]
  implicit val keyAvroSerdeKafkaBytes: KeySerde[KBytes] = KSerdes.Bytes().asInstanceOf[KeySerde[KBytes]]
  implicit val keyAvroSerdeFloat: KeySerde[Float] = KSerdes.Float().asInstanceOf[KeySerde[Float]]
  implicit val keyAvroSerdeJavaFloat: KeySerde[JFloat] = KSerdes.Float().asInstanceOf[KeySerde[JFloat]]
  implicit val keyAvroSerdeDouble: KeySerde[Double] = KSerdes.Double().asInstanceOf[KeySerde[Double]]
  implicit val keyAvroSerdeJavaDouble: KeySerde[JDouble] = KSerdes.Double().asInstanceOf[KeySerde[JDouble]]
  implicit val keyAvroSerdeInt: KeySerde[Int] = KSerdes.Integer().asInstanceOf[KeySerde[Int]]
  implicit val keyAvroSerdeJavaInt: KeySerde[JInt] = KSerdes.Integer().asInstanceOf[KeySerde[JInt]]
  implicit val keyAvroSerdeJavaUUID: KeySerde[ju.UUID] = KSerdes.UUID().asInstanceOf[KeySerde[ju.UUID]]

  implicit val valueAvroSerdeString: ValueSerde[String] = KSerdes.String().asInstanceOf[ValueSerde[String]]
  implicit val valueAvroSerdeLong: ValueSerde[Long] = KSerdes.Long().asInstanceOf[ValueSerde[Long]]
  implicit val valueAvroSerdeJavaLong: ValueSerde[JLong] = KSerdes.Long().asInstanceOf[ValueSerde[JLong]]
  implicit val valueAvroSerdeByteArray: ValueSerde[Array[Byte]] = KSerdes.ByteArray().asInstanceOf[ValueSerde[Array[Byte]]]
  implicit val valueAvroSerdeKafkaBytes: ValueSerde[KBytes] = KSerdes.Bytes().asInstanceOf[ValueSerde[KBytes]]
  implicit val valueAvroSerdeFloat: ValueSerde[Float] = KSerdes.Float().asInstanceOf[ValueSerde[Float]]
  implicit val valueAvroSerdeJavaFloat: ValueSerde[JFloat] = KSerdes.Float().asInstanceOf[ValueSerde[JFloat]]
  implicit val valueAvroSerdeDouble: ValueSerde[Double] = KSerdes.Double().asInstanceOf[ValueSerde[Double]]
  implicit val valueAvroSerdeJavaDouble: ValueSerde[JDouble] = KSerdes.Double().asInstanceOf[ValueSerde[JDouble]]
  implicit val valueAvroSerdeInt: ValueSerde[Int] = KSerdes.Integer().asInstanceOf[ValueSerde[Int]]
  implicit val valueAvroSerdeJavaInt: ValueSerde[JInt] = KSerdes.Integer().asInstanceOf[ValueSerde[JInt]]
  implicit val valueAvroSerdeJavaUUID: ValueSerde[ju.UUID] = KSerdes.UUID().asInstanceOf[ValueSerde[ju.UUID]]

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

  private def kafkaSerde[T >: Null](
    genericAvroSerde: GenericAvroSerde,
    recordFormat: RecordFormat[T]
  ): KSerde[T] = {
    KSerdes.serdeFrom(
      new Serializer[T] {
        override def serialize(topic: String, data: T): Array[Byte] = {
          if (data == null) null
          else genericAvroSerde.serializer().serialize(topic, recordFormat.to(data))
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

  private def genericAvroSerde(config: AvroSerdeConfig, forKey: Boolean): GenericAvroSerde = {
    val serde = new GenericAvroSerde()
    serde.configure(config.toMap.asJava, forKey)
    serde
  }

}
