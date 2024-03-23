package ru.itmo.service

import kotlinx.coroutines.*
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import ru.itmo.dao.UsersInfoDao
import ru.itmo.model.TransactionStatus

@Component
class CreatedTransactionHandler(
    private val usersInfoDao: UsersInfoDao,
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
        val transactions = usersInfoDao
            .selectTransactionsByStatus(TransactionStatus.CREATED.name).collectList().awaitSingle()
        transactions.forEach { transaction ->
            val balance = usersInfoDao.getUserBalance(transaction.userId).awaitSingle()
            if (balance - transaction.amount > 0) {
                usersInfoDao.updateUserBalance(userId = transaction.userId, balance - transaction.amount).awaitSingle()
                usersInfoDao.updateTransactionStatus(transaction.id, TransactionStatus.CREATED.name).awaitSingle()
            } else {
                usersInfoDao.updateTransactionStatus(transaction.id, TransactionStatus.ERROR.name).awaitSingle()
            }
        }

        delay(10000)
    }
}