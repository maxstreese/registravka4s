package com.streese.registravka4s

import com.sksamuel.avro4s.Record
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord

private[registravka4s] class WrappedGenericRecord(val record: Record) extends GenericRecord {
  override def put(key: String, v: Any): Unit = record.put(key, v)
  override def get(key: String): AnyRef       = record.get(key)
  override def put(i: Int, v: Any): Unit      = record.put(i, v)
  override def get(i: Int): AnyRef            = record.get(i)
  override def getSchema: Schema              = record.getSchema
}
