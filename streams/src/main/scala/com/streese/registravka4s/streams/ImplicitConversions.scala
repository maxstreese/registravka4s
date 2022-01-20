package com.streese.registravka4s.streams

import com.streese.registravka4s.KeySerde
import com.streese.registravka4s.ValueSerde
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.{CogroupedKStream => CogroupedKStreamJ}
import org.apache.kafka.streams.kstream.{KGroupedStream => KGroupedStreamJ}
import org.apache.kafka.streams.kstream.{KGroupedTable => KGroupedTableJ}
import org.apache.kafka.streams.kstream.{KStream => KStreamJ}
import org.apache.kafka.streams.kstream.{KTable => KTableJ}
import org.apache.kafka.streams.kstream.{SessionWindowedCogroupedKStream => SessionWindowedCogroupedKStreamJ}
import org.apache.kafka.streams.kstream.{SessionWindowedKStream => SessionWindowedKStreamJ}
import org.apache.kafka.streams.kstream.{TimeWindowedCogroupedKStream => TimeWindowedCogroupedKStreamJ}
import org.apache.kafka.streams.kstream.{TimeWindowedKStream => TimeWindowedKStreamJ}
import org.apache.kafka.streams.processor.StateStore
import org.apache.kafka.streams.scala.kstream._

/**
 * This is essentially just a copy of [[org.apache.kafka.streams.scala.ImplicitConversions]] with the difference that
 * every occurence of an implicit [[org.apache.kafka.common.serialization.Serde]] is replaced with either a
 * [[com.streese.registravka4s.KeySerde]] or a [[com.streese.registravka4s.ValueSerde]] depending on whether the
 * type is used for a key or for a value.
 *
 * Original documentation:
 * Implicit conversions between the Scala wrapper objects and the underlying Java
 * objects.
 */
object ImplicitConversions {

  implicit def wrapKStream[K, V](
    inner: KStreamJ[K, V]
  ): KStream[K, V] = new KStream[K, V](inner)

  implicit def wrapKGroupedStream[K, V](
    inner: KGroupedStreamJ[K, V]
  ): KGroupedStream[K, V] = new KGroupedStream[K, V](inner)

  implicit def wrapTimeWindowedKStream[K, V](
    inner: TimeWindowedKStreamJ[K, V]
  ): TimeWindowedKStream[K, V] = new TimeWindowedKStream[K, V](inner)

  implicit def wrapSessionWindowedKStream[K, V](
    inner: SessionWindowedKStreamJ[K, V]
  ): SessionWindowedKStream[K, V] = new SessionWindowedKStream[K, V](inner)

  implicit def wrapCogroupedKStream[K, V](
    inner: CogroupedKStreamJ[K, V]
  ): CogroupedKStream[K, V] = new CogroupedKStream[K, V](inner)

  implicit def wrapTimeWindowedCogroupedKStream[K, V](
    inner: TimeWindowedCogroupedKStreamJ[K, V]
  ): TimeWindowedCogroupedKStream[K, V] = new TimeWindowedCogroupedKStream[K, V](inner)

  implicit def wrapSessionWindowedCogroupedKStream[K, V](
    inner: SessionWindowedCogroupedKStreamJ[K, V]
  ): SessionWindowedCogroupedKStream[K, V] = new SessionWindowedCogroupedKStream[K, V](inner)

  implicit def wrapKTable[K, V](
    inner: KTableJ[K, V]
  ): KTable[K, V] = new KTable[K, V](inner)

  implicit def wrapKGroupedTable[K, V](
    inner: KGroupedTableJ[K, V]
  ): KGroupedTable[K, V] = new KGroupedTable[K, V](inner)

  implicit def tuple2ToKeyValue[K, V](
    tuple: (K, V)
  ): KeyValue[K, V] = new KeyValue(tuple._1, tuple._2)

  // we would also like to allow users implicit serdes
  // and these implicits will convert them to `Grouped`, `Produced` or `Consumed`

  implicit def consumedFromSerde[K, V](
    implicit
    keySerde: KeySerde[K],
    valueSerde: ValueSerde[V]
  ): Consumed[K, V] = Consumed.`with`[K, V]

  implicit def groupedFromSerde[K, V](
    implicit
    keySerde: KeySerde[K],
    valueSerde: ValueSerde[V]
  ): Grouped[K, V] = Grouped.`with`[K, V]

  implicit def joinedFromKeyValueOtherSerde[K, V, VO](
    implicit
    keySerde: KeySerde[K],
    valueSerde: ValueSerde[V],
    otherValueSerde: ValueSerde[VO]
  ): Joined[K, V, VO] = Joined.`with`[K, V, VO]

  implicit def materializedFromSerde[K, V, S <: StateStore](
    implicit
    keySerde: KeySerde[K],
    valueSerde: ValueSerde[V]
  ): Materialized[K, V, S] = Materialized.`with`[K, V, S]

  implicit def producedFromSerde[K, V](
    implicit
    keySerde: KeySerde[K],
    valueSerde: ValueSerde[V]
  ): Produced[K, V] = Produced.`with`[K, V]

  implicit def repartitionedFromSerde[K, V](
    implicit
    keySerde: KeySerde[K],
    valueSerde: ValueSerde[V]
  ): Repartitioned[K, V] = Repartitioned.`with`[K, V]

  implicit def streamJoinFromKeyValueOtherSerde[K, V, VO](
    implicit
    keySerde: KeySerde[K],
    valueSerde: ValueSerde[V],
    otherValueSerde: KeySerde[VO]
  ): StreamJoined[K, V, VO] = StreamJoined.`with`[K, V, VO]

}
