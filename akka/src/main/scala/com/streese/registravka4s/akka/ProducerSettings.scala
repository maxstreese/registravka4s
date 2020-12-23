package com.streese.registravka4s.akka

import akka.kafka.{ProducerSettings => AkkaProducerSettings}
import com.streese.registravka4s.{KeySerde, KeySerializer, ValueSerde, ValueSerializer}
import com.typesafe.config.Config

object ProducerSettings {

  def apply[K: KeySerde, V: ValueSerde](config: Config): AkkaProducerSettings[K, V] =
    AkkaProducerSettings(config, KeySerializer[K], ValueSerializer[V])

  def apply[K: KeySerde, V: ValueSerde](system: akka.actor.ActorSystem): AkkaProducerSettings[K, V] =
    apply(system.settings.config.getConfig(AkkaProducerSettings.configPath))

  def apply[K: KeySerde, V: ValueSerde](system: akka.actor.ClassicActorSystemProvider): AkkaProducerSettings[K, V] =
    apply(system.classicSystem)

}
