package ru.itmo.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.itmo.service.PaymentProcessorService
import ru.itmo.sper.bank.model.PaymentRequest
import java.util.UUID

@RestController
class PaymentController(
    private val paymentProcessorService: PaymentProcessorService
) {

    @PostMapping("/kachalka-payment")
    fun processKachalkaPayment(
        @RequestBody paymentRequest: PaymentRequest,
    ): Mono<UUID> = paymentProcessorService.processPayment(paymentRequest)

}