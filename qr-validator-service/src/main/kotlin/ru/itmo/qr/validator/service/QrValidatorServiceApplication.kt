package ru.itmo.qr.validator.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class QrValidatorServiceApplication

fun main(args: Array<String>) {
    runApplication<QrValidatorServiceApplication>(*args)
}