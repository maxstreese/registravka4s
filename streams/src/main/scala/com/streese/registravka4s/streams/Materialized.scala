package com.streese.registravka4s.streams

import com.streese.registravka4s.KeySerde
import com.streese.registravka4s.ValueSerde
import org.apache.kafka.streams.kstream.{Materialized => MaterializedJ}
import org.apache.kafka.streams.processor.StateStore
import org.apache.kafka.streams.scala.ByteArrayKeyValueStore
import org.apache.kafka.streams.scala.ByteArraySessionStore
import org.apache.kafka.streams.scala.ByteArrayWindowStore
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier
import org.apache.kafka.streams.state.SessionBytesStoreSupplier
import org.apache.kafka.streams.state.WindowBytesStoreSupplier

/**
  * This is essentially just a copy of [[org.apache.kafka.streams.scala.kstream.Materialized]] with the difference
  * that every occurence of an implicit [[org.apache.kafka.common.serialization.Serde]] is replaced with either a
  * [[com.streese.registravka4s.KeySerde]] or a [[com.streese.registravka4s.ValueSerde]] depending on whether the
  * type is used for a key or for a value.
  *
  * Additionally the `with` method was removed as this should never be called directly by client code and is hence
  * not necessary to be adjusted with respect to the serdes.
  */
object Materialized {

  /**
   * Materialize a [[StateStore]] with the given name.
   *
   * @tparam K         key type of the store
   * @tparam V         value type of the store
   * @tparam S         type of the [[StateStore]]
   * @param storeName  the name of the underlying [[org.apache.kafka.streams.scala.kstream.KTable]] state store;
   *                   valid characters are ASCII alphanumerics, '.', '_' and '-'.
   * @param keySerde   the key serde to use.
   * @param valueSerde the value serde to use.
   * @return a new [[Materialized]] instance with the given storeName
   */
  def as[K, V, S <: StateStore](storeName: String)(implicit keySerde: KeySerde[K],
                                                   valueSerde: ValueSerde[V]): MaterializedJ[K, V, S] =
    MaterializedJ.as(storeName).withKeySerde(keySerde).withValueSerde(valueSerde)

  /**
   * Materialize a [[org.apache.kafka.streams.state.WindowStore]] using the provided [[WindowBytesStoreSupplier]].
   *
   * Important: Custom subclasses are allowed here, but they should respect the retention contract:
   * Window stores are required to retain windows at least as long as (window size + window grace period).
   * Stores constructed via [[org.apache.kafka.streams.state.Stores]] already satisfy this contract.
   *
   * @tparam K         key type of the store
   * @tparam V         value type of the store
   * @param supplier   the [[WindowBytesStoreSupplier]] used to materialize the store
   * @param keySerde   the key serde to use.
   * @param valueSerde the value serde to use.
   * @return a new [[Materialized]] instance with the given supplier
   */
  def as[K, V](supplier: WindowBytesStoreSupplier)(implicit keySerde: KeySerde[K],
                                                   valueSerde: ValueSerde[V]): MaterializedJ[K, V, ByteArrayWindowStore] =
    MaterializedJ.as(supplier).withKeySerde(keySerde).withValueSerde(valueSerde)

  /**
   * Materialize a [[org.apache.kafka.streams.state.SessionStore]] using the provided [[SessionBytesStoreSupplier]].
   *
   * Important: Custom subclasses are allowed here, but they should respect the retention contract:
   * Session stores are required to retain windows at least as long as (session inactivity gap + session grace period).
   * Stores constructed via [[org.apache.kafka.streams.state.Stores]] already satisfy this contract.
   *
   * @tparam K         key type of the store
   * @tparam V         value type of the store
   * @param supplier   the [[SessionBytesStoreSupplier]] used to materialize the store
   * @param keySerde   the key serde to use.
   * @param valueSerde the value serde to use.
   * @return a new [[Materialized]] instance with the given supplier
   */
  def as[K, V](supplier: SessionBytesStoreSupplier)(implicit keySerde: KeySerde[K],
                                                    valueSerde: ValueSerde[V]): MaterializedJ[K, V, ByteArraySessionStore] =
    MaterializedJ.as(supplier).withKeySerde(keySerde).withValueSerde(valueSerde)

  /**
   * Materialize a [[org.apache.kafka.streams.state.KeyValueStore]] using the provided [[KeyValueBytesStoreSupplier]].
   *
   * @tparam K         key type of the store
   * @tparam V         value type of the store
   * @param supplier   the [[KeyValueBytesStoreSupplier]] used to materialize the store
   * @param keySerde   the key serde to use.
   * @param valueSerde the value serde to use.
   * @return a new [[Materialized]] instance with the given supplier
   */
  def as[K, V](
    supplier: KeyValueBytesStoreSupplier
  )(implicit keySerde: KeySerde[K], valueSerde: ValueSerde[V]): MaterializedJ[K, V, ByteArrayKeyValueStore] =
    MaterializedJ.as(supplier).withKeySerde(keySerde).withValueSerde(valueSerde)

}
