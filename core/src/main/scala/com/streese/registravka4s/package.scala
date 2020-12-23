package com.streese

import org.apache.kafka.common.serialization.{Serde, Serializer, Deserializer}

package object registravka4s {

  sealed trait IsKeySerde
  type KeySerde[T] = Serde[T] with IsKeySerde

  sealed trait IsValueSerde
  type ValueSerde[T] = Serde[T] with IsValueSerde

  object KeySerializer {
    def apply[T](implicit serde: KeySerde[T]): Serializer[T] = serde.serializer()
  }

  object ValueSerializer {
    def apply[T](implicit serde: ValueSerde[T]): Serializer[T] = serde.serializer()
  }

  object KeyDeserializer {
    def apply[T](implicit serde: KeySerde[T]): Deserializer[T] = serde.deserializer()
  }

  object ValueDeserializer {
    def apply[T](implicit serde: ValueSerde[T]): Deserializer[T] = serde.deserializer()
  }

}
