package ru.itmo.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("rsa")
data class RsaKeyProperties(
    val keyPath: String,
)