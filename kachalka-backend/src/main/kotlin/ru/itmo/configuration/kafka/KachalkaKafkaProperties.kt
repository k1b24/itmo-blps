package ru.itmo.configuration.kafka

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("listener")
data class KachalkaKafkaProperties(
    val certificateBoughtTopic: String,
)