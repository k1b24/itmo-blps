package ru.itmo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("ru.itmo.configuration")
class KachalkaBackendApplication

fun main(args: Array<String>) {
    runApplication<KachalkaBackendApplication>(*args)
}
