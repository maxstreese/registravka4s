package com.streese.registravka4s

import com.sksamuel.avro4s.{Encoder, Decoder, RecordFormat, SchemaFor}

trait GenericRecordFormat {

  implicit def avroRecordFormatGeneric[T: Encoder: Decoder: SchemaFor]: RecordFormat[T] = RecordFormat[T]

}

object GenericRecordFormat {

  object Implicits extends GenericRecordFormat

}
