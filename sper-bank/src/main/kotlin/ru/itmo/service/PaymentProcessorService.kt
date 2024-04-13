package ru.itmo.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import ru.itmo.dao.UsersInfoDao
import ru.itmo.sper.bank.model.TransactionStatus
import ru.itmo.model.exception.InvalidCardInfoException
import ru.itmo.sper.bank.model.PaymentRequest
import java.util.*

@Service
class PaymentProcessorService(
    private val usersInfoDao: UsersInfoDao
) {

    fun  processPayment(paymentRequest: PaymentRequest): Mono<UUID> =
        usersInfoDao.findUserId(
            paymentRequest.creditCardNumber,
            paymentRequest.expirationDate,
            paymentRequest.cvvCode,
        )
            .switchIfEmpty {
                Mono.error(InvalidCardInfoException("Card data is invalid"))
            }
            .flatMap { userId ->
                usersInfoDao.createTransaction(
                    userId = userId,
                    status = TransactionStatus.CREATED.name,
                    amount = paymentRequest.amount,
                )
            }
}