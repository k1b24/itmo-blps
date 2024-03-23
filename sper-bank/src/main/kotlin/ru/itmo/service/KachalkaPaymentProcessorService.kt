package ru.itmo.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import ru.itmo.dao.UsersInfoDao
import ru.itmo.sper.bank.model.TransactionStatus
import ru.itmo.model.exception.InvalidCardInfoException
import ru.itmo.sper.bank.model.KachalkaPaymentRequest
import java.util.*

@Service
class KachalkaPaymentProcessorService(
    private val usersInfoDao: UsersInfoDao
) {

    fun processPayment(kachalkaPaymentRequest: KachalkaPaymentRequest): Mono<UUID> =
        usersInfoDao.findUserId(
            kachalkaPaymentRequest.creditCardNumber,
            kachalkaPaymentRequest.expirationDate,
            kachalkaPaymentRequest.cvvCode,
        )
            .switchIfEmpty {
                Mono.error(InvalidCardInfoException("Card data is invalid"))
            }
            .flatMap { userId ->
                usersInfoDao.createTransaction(
                    userId = userId,
                    status = TransactionStatus.CREATED.name,
                    amount = kachalkaPaymentRequest.amount,
                )
            }
}