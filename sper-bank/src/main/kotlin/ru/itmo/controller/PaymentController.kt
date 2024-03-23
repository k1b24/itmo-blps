package ru.itmo.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.itmo.service.KachalkaPaymentProcessorService
import ru.itmo.sper.bank.model.KachalkaPaymentRequest
import java.util.UUID

@RestController
class PaymentController(
    private val kachalkaPaymentProcessorService: KachalkaPaymentProcessorService
) {

    @PostMapping("/kachalka-payment")
    fun processKachalkaPayment(
        @RequestBody kachalkaPaymentRequest: KachalkaPaymentRequest,
    ): Mono<UUID> = kachalkaPaymentProcessorService.processPayment(kachalkaPaymentRequest)

}