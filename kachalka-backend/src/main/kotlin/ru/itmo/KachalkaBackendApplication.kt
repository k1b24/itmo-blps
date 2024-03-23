package ru.itmo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KachalkaBackendApplication

fun main(args: Array<String>) {
    runApplication<KachalkaBackendApplication>(*args)
}
