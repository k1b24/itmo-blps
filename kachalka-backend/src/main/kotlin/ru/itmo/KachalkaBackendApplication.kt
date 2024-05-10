package ru.itmo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@ConfigurationPropertiesScan("ru.itmo.configuration")
@EnableScheduling
class KachalkaBackendApplication

fun main(args: Array<String>) {
    runApplication<KachalkaBackendApplication>(*args)
}
