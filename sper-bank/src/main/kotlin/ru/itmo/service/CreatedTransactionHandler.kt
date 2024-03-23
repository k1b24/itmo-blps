package ru.itmo.service

import kotlinx.coroutines.*
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import ru.itmo.dao.UsersInfoDao
import ru.itmo.sper.bank.model.TransactionStatus

@Component
class CreatedTransactionHandler(
    private val usersInfoDao: UsersInfoDao,
    private val kachalkaWebClient: WebClient,
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    @EventListener(ApplicationReadyEvent::class)
    fun start() {
        scope.launch {
            startTransactionsHandling()
        }
    }

    private suspend fun startTransactionsHandling() {
        println("started transactions handling")
        while (true) {
            try {
                val transactions = usersInfoDao
                    .selectTransactionsByStatus(TransactionStatus.CREATED.name).collectList().awaitSingle()
                transactions.forEach { transaction ->
                    val balance = usersInfoDao.getUserBalance(transaction.userId).awaitSingle()
                    if (balance - transaction.amount >= 0) {
                        usersInfoDao.updateUserBalance(transaction.userId, balance - transaction.amount).awaitFirstOrNull()
                        kachalkaWebClient.post().uri("/transactions/${transaction.id}/status/${TransactionStatus.SUCCESS.name}").retrieve().toBodilessEntity().awaitFirstOrNull()
                        usersInfoDao.updateTransactionStatus(transaction.id, TransactionStatus.SUCCESS.name).awaitFirstOrNull()
                    } else {
                        kachalkaWebClient.post().uri("/transactions/${transaction.id}/status/${TransactionStatus.ERROR.name}").retrieve().toBodilessEntity().awaitFirstOrNull()
                        usersInfoDao.updateTransactionStatus(transaction.id, TransactionStatus.ERROR.name).awaitFirstOrNull()
                    }
                }
            } catch (exception: Exception) {
                println("Exception: ${exception.message}")
            }
            delay(10000)
        }
    }
}