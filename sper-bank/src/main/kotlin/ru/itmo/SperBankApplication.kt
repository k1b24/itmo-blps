package ru.itmo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SperBankApplication

fun main(args: Array<String>) {
    runApplication<SperBankApplication>(*args)
}