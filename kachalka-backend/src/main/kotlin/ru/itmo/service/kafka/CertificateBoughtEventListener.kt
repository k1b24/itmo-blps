package ru.itmo.service.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.retry.annotation.Backoff
import org.springframework.stereotype.Service
import ru.itmo.model.event.CertificateBoughtEvent
import ru.itmo.service.CertificatesEmailSenderService
import ru.itmo.service.qr.QrCodeGeneratorService

@Service
class CertificateBoughtEventListener(
    val certificatesEmailSender: CertificatesEmailSenderService,
) {

    @KafkaListener(
        topics = ["kachalka-user-certificate-bought-event"],
        id = "ashalet",
    )
    @RetryableTopic(
        attempts = "2",
        numPartitions = "2",
        backoff = Backoff(delay = 1000, maxDelay = 1000)
    )
    fun handle(event: CertificateBoughtEvent) {
        println(event)
        certificatesEmailSender.sendCertificateBoughtNotificationEmail(
            event.login, event.email, event.certificateId
        )
    }
}