package com.streese.registravka4s.akka

import akka.kafka.{ConsumerSettings => AkkaConsumerSettings}
import com.streese.registravka4s.{KeySerde, KeyDeserializer, ValueSerde, ValueDeserializer}
import com.typesafe.config.Config

object ConsumerSettings {

  def apply[K: KeySerde, V: ValueSerde](config: Config): AkkaConsumerSettings[K, V] =
    AkkaConsumerSettings(config, KeyDeserializer[K], ValueDeserializer[V])

  def apply[K: KeySerde, V: ValueSerde](system: akka.actor.ActorSystem): AkkaConsumerSettings[K, V] =
    apply(system.settings.config.getConfig(AkkaConsumerSettings.configPath))

  def apply[K: KeySerde, V: ValueSerde](system: akka.actor.ClassicActorSystemProvider): AkkaConsumerSettings[K, V] =
    apply(system.classicSystem)

}
