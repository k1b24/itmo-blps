package ru.itmo.configuration.kafka

import io.micrometer.core.instrument.MeterRegistry
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.MicrometerConsumerListener
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.kafka.support.serializer.JsonDeserializer
import reactor.kafka.sender.SenderOptions
import ru.itmo.model.event.CertificateBoughtEvent


@EnableKafka
@Configuration
class KafkaConfiguration {

    @Bean
    fun certificateBoughtEventsProducer(
        properties: KafkaProperties,
    ): ReactiveKafkaProducerTemplate<String, CertificateBoughtEvent> {
        val props = properties.buildProducerProperties()
        return ReactiveKafkaProducerTemplate<String, CertificateBoughtEvent>(SenderOptions.create(props))
    }

    @Bean
    fun kafkaListenerContainerFactory(
        properties: KafkaProperties,
        meterRegistry: MeterRegistry,
    ): ConcurrentKafkaListenerContainerFactory<String, CertificateBoughtEvent> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, CertificateBoughtEvent>()
        val factoryProperties = properties.buildConsumerProperties()
        factoryProperties[JsonDeserializer.TRUSTED_PACKAGES] = "ru.itmo.model.event"
        factory.consumerFactory = DefaultKafkaConsumerFactory<String, CertificateBoughtEvent>(factoryProperties)
            .apply {
                addListener(MicrometerConsumerListener(meterRegistry))
            }
        factory.setConcurrency(3)
        factory.containerProperties.pollTimeout = 3000
        return factory
    }
}