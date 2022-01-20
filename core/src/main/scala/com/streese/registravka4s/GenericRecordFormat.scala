package com.streese.registravka4s

import com.sksamuel.avro4s.Decoder
import com.sksamuel.avro4s.Encoder
import com.sksamuel.avro4s.RecordFormat

trait GenericRecordFormat {

  implicit def avroRecordFormatGeneric[T: Encoder: Decoder]: RecordFormat[T] = RecordFormat[T]

}

object GenericRecordFormat {

  object Implicits extends GenericRecordFormat

}
