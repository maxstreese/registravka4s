package com.streese.registravka4s

import org.apache.kafka.common.serialization.Serdes

trait PrimitiveSerdes {

  implicit val keyAvroSerdeString: KeySerde[String] =
    Serdes.String().asInstanceOf[KeySerde[String]]
  implicit val keyAvroSerdeLong: KeySerde[Long] =
    Serdes.Long().asInstanceOf[KeySerde[Long]]
  implicit val keyAvroSerdeJavaLong: KeySerde[java.lang.Long] =
    Serdes.Long().asInstanceOf[KeySerde[java.lang.Long]]
  implicit val keyAvroSerdeByteArray: KeySerde[Array[Byte]] =
    Serdes.ByteArray().asInstanceOf[KeySerde[Array[Byte]]]
  implicit val keyAvroSerdeKafkaBytes: KeySerde[org.apache.kafka.common.utils.Bytes] =
    Serdes.Bytes().asInstanceOf[KeySerde[org.apache.kafka.common.utils.Bytes]]
  implicit val keyAvroSerdeFloat: KeySerde[Float] =
    Serdes.Float().asInstanceOf[KeySerde[Float]]
  implicit val keyAvroSerdeJavaFloat: KeySerde[java.lang.Float] =
    Serdes.Float().asInstanceOf[KeySerde[java.lang.Float]]
  implicit val keyAvroSerdeDouble: KeySerde[Double] =
    Serdes.Double().asInstanceOf[KeySerde[Double]]
  implicit val keyAvroSerdeJavaDouble: KeySerde[java.lang.Double] =
    Serdes.Double().asInstanceOf[KeySerde[java.lang.Double]]
  implicit val keyAvroSerdeInteger: KeySerde[Int] =
    Serdes.Integer().asInstanceOf[KeySerde[Int]]
  implicit val keyAvroSerdeJavaInteger: KeySerde[java.lang.Integer] =
    Serdes.Integer().asInstanceOf[KeySerde[java.lang.Integer]]
  implicit val keyAvroSerdeJavaUUID: KeySerde[java.util.UUID] =
    Serdes.UUID().asInstanceOf[KeySerde[java.util.UUID]]

  implicit val valueAvroSerdeString: ValueSerde[String] =
    Serdes.String().asInstanceOf[ValueSerde[String]]
  implicit val valueAvroSerdeLong: ValueSerde[Long] =
    Serdes.Long().asInstanceOf[ValueSerde[Long]]
  implicit val valueAvroSerdeJavaLong: ValueSerde[java.lang.Long] =
    Serdes.Long().asInstanceOf[ValueSerde[java.lang.Long]]
  implicit val valueAvroSerdeByteArray: ValueSerde[Array[Byte]] =
    Serdes.ByteArray().asInstanceOf[ValueSerde[Array[Byte]]]
  implicit val valueAvroSerdeKafkaBytes: ValueSerde[org.apache.kafka.common.utils.Bytes] =
    Serdes.Bytes().asInstanceOf[ValueSerde[org.apache.kafka.common.utils.Bytes]]
  implicit val valueAvroSerdeFloat: ValueSerde[Float] =
    Serdes.Float().asInstanceOf[ValueSerde[Float]]
  implicit val valueAvroSerdeJavaFloat: ValueSerde[java.lang.Float] =
    Serdes.Float().asInstanceOf[ValueSerde[java.lang.Float]]
  implicit val valueAvroSerdeDouble: ValueSerde[Double] =
    Serdes.Double().asInstanceOf[ValueSerde[Double]]
  implicit val valueAvroSerdeJavaDouble: ValueSerde[java.lang.Double] =
    Serdes.Double().asInstanceOf[ValueSerde[java.lang.Double]]
  implicit val valueAvroSerdeInteger: ValueSerde[Int] =
    Serdes.Integer().asInstanceOf[ValueSerde[Int]]
  implicit val valueAvroSerdeJavaInteger: ValueSerde[java.lang.Integer] =
    Serdes.Integer().asInstanceOf[ValueSerde[java.lang.Integer]]
  implicit val valueAvroSerdeJavaUUID: ValueSerde[java.util.UUID] =
    Serdes.UUID().asInstanceOf[ValueSerde[java.util.UUID]]

}

object PrimitiveSerdes {

  object Implicits extends PrimitiveSerdes

}
