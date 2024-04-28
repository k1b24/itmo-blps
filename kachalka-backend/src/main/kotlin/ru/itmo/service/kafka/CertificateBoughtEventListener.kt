package ru.itmo.service.kafka

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import ru.itmo.model.event.CertificateBoughtEvent

@Service
class CertificateBoughtEventListener {

    @KafkaListener(
        topics = ["kachalka-user-certificate-bought-event"],
        id = "ashalet",
    )
    fun handle(event: CertificateBoughtEvent) {
        println(event)
    }

}