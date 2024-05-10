package ru.itmo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("ru.itmo.configuration")
class SperBankApplication

fun main(args: Array<String>) {
    runApplication<SperBankApplication>(*args)
}