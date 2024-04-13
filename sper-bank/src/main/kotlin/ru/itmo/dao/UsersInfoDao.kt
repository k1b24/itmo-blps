package ru.itmo.dao

import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.model.TransactionEntity
import ru.itmo.sper.bank.model.TransactionStatus
import ru.itmo.util.getOrThrow
import java.util.UUID

@Component
class UsersInfoDao(
    val databaseClient: DatabaseClient,
) {

    fun createUser(
        creditCardNumber: String,
        expirationDate: String,
        cvvCode: String,
        balance: Float,
    ): Mono<UUID> {
        databaseClient.inConnection {
            it.beginTransaction()
                it.commitTransaction()
        }
        val userId = UUID.randomUUID()
        return databaseClient.sql(
            """
                INSERT INTO bank_users_data (user_id, credit_card_number, expiration_date, cvv_code, balance) 
                    VALUES (:user_id, :credit_card_number, :expiration_date, :cvv_code, :balance)
            """.trimIndent()
        )
            .bind("user_id", userId)
            .bind("credit_card_number", creditCardNumber)
            .bind("expiration_date", expirationDate)
            .bind("cvv_code", cvvCode)
            .bind("balance", balance)
            .then()
            .then(Mono.just(userId))
    }

    fun findUserId(
        creditCardNumber: String,
        expirationDate: String,
        cvvCode: String,
    ): Mono<UUID> = databaseClient.sql(
        """
            SELECT user_id FROM bank_users_data WHERE credit_card_number = :credit_card_number AND 
                expiration_date = :expiration_date AND cvv_code = :cvv_code
        """.trimIndent()
    )
        .bind("credit_card_number", creditCardNumber)
        .bind("expiration_date", expirationDate)
        .bind("cvv_code", cvvCode)
        .map { row, _ ->
            row.getOrThrow<UUID>("user_id")
        }
        .first()

    fun createTransaction(
        userId: UUID,
        status: String,
        amount: Float,
    ): Mono<UUID> {
        val transactionId = UUID.randomUUID()
        return databaseClient.sql(
            """
                INSERT INTO transaction_status (id, user_id, status, amount) VALUES (:id, :user_id, :status, :amount)
            """.trimIndent()
        )
            .bind("id", transactionId)
            .bind("user_id", userId)
            .bind("status", status)
            .bind("amount", amount)
            .then()
            .then(Mono.just(transactionId))
    }

    fun selectTransactionsByStatus(
        status: String,
    ): Flux<TransactionEntity> = databaseClient.sql(
        """
            SELECT * FROM transaction_status WHERE status = :status
        """.trimIndent()
    )
        .bind("status", status)
        .map { row, _ ->
            TransactionEntity(
                id = row.getOrThrow("id"),
                userId = row.getOrThrow("user_id"),
                status = TransactionStatus.valueOf(row.getOrThrow("status")),
                amount = row.getOrThrow("amount"),
            )
        }
        .all()

    fun updateTransactionStatus(
        transactionId: UUID,
        newStatus: String,
    ): Mono<Void> = databaseClient.sql(
        """
            UPDATE transaction_status SET status = :status WHERE id = :id
        """.trimIndent()
    )
        .bind("id", transactionId)
        .bind("status", newStatus)
        .then()

    fun getUserBalance(userId: UUID): Mono<Float> = databaseClient.sql(
        """
            SELECT balance FROM bank_users_data WHERE user_id = :user_id
        """.trimIndent()
    )
        .bind("user_id", userId)
        .map { row, _ ->
            row.getOrThrow<Float>("balance")
        }
        .first()

    fun updateUserBalance(userId: UUID, newBalance: Float): Mono<Void> = databaseClient.sql(
        "UPDATE bank_users_data SET balance = :balance WHERE user_id = :user_id"
    )
        .bind("balance", newBalance)
        .bind("user_id", userId)
        .then()
}