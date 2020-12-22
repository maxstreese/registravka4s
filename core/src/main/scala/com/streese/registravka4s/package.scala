package com.streese

import org.apache.kafka.common.serialization.Serde

package object registravka4s {

  sealed trait IsKeySerde
  type KeySerde[T] = Serde[T] with IsKeySerde

  sealed trait IsValueSerde
  type ValueSerde[T] = Serde[T] with IsValueSerde

}
