package com.streese.registravka4s.benchmarks

import org.apache.avro.specific.SpecificRecord
import org.apache.avro.generic.GenericRecord
import java.io.ByteArrayOutputStream
import org.apache.avro.specific.SpecificDatumWriter
import org.apache.avro.generic.GenericDatumWriter
import org.apache.avro.io.EncoderFactory

object Avro4sBenchmarkSetup {

  case class County(name: String, towns: Seq[Town], ceremonial: Boolean, lat: Double, long: Double)
  case class Town(name: String, population: Int)

  private val encoderFactory = EncoderFactory.get()

  def emulateEncodingForSpecificRecord(obj: SpecificRecord): Array[Byte] = {
    val out = new ByteArrayOutputStream
    val writer = new SpecificDatumWriter[AnyRef](obj.getSchema)
    val encoder = encoderFactory.directBinaryEncoder(out, null)
    writer.write(obj, encoder)
    encoder.flush()
    val result = out.toByteArray
    out.close()
    result
  }

  def emulateEncodingForGenericRecord(obj: GenericRecord): Array[Byte] = {
    val out = new ByteArrayOutputStream
    val writer = new GenericDatumWriter[AnyRef](obj.getSchema)
    val encoder = encoderFactory.directBinaryEncoder(out, null)
    writer.write(obj, encoder)
    encoder.flush()
    val result = out.toByteArray
    out.close()
    result
  }

}
