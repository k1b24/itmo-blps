package ru.itmo.controller

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.itmo.service.UserCertificatesService
import ru.itmo.sper.bank.model.TransactionStatus
import java.util.*

@RestController
class SperBankTransactionCallbackController(
    private val userCertificatesService: UserCertificatesService,
) {

    @PostMapping("/transactions/{transaction-id}/status/{transaction-status}")
    fun updateTransactionStatus(
        @PathVariable("transaction-id") transactionId: UUID,
        @PathVariable("transaction-status") transactionStatus: TransactionStatus,
    ): Mono<Void> = userCertificatesService.handleTransactionStatusUpdated(transactionId, transactionStatus)

}