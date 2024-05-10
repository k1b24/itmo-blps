package ru.itmo.dao

import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import ru.itmo.model.CertificateTransactionEntity
import ru.itmo.sper.bank.model.TransactionStatus
import ru.itmo.util.getOrThrow
import java.util.*

@Component
class CertificatesTransactionsDao(
    private val databaseClient: DatabaseClient,
) {

    fun insertTransactionInfo(
        transactionId: UUID,
        userLogin: String,
        userEmail: String,
        certificateId: UUID,
        transactionStatus: String,
    ): Mono<Void> = databaseClient.sql(
        """
            INSERT INTO certificates_transactions (transaction_id, user_login, user_email, certificate_id, transaction_status) 
                VALUES (:transaction_id, :user_login, :user_email, :certificate_id, :transaction_status)
        """.trimIndent()
    )
        .bind("transaction_id", transactionId)
        .bind("user_login", userLogin)
        .bind("user_email", userEmail)
        .bind("certificate_id", certificateId)
        .bind("transaction_status", transactionStatus)
        .then()

    fun getCertificateTransaction(transactionId: UUID): Mono<CertificateTransactionEntity> = databaseClient.sql(
        """
            SELECT * FROM certificates_transactions WHERE transaction_id = :transaction_id
        """.trimIndent()
    )
        .bind("transaction_id", transactionId)
        .map { row, _ ->
            CertificateTransactionEntity(
                transactionId = row.getOrThrow("transaction_id"),
                userLogin = row.getOrThrow("user_login"),
                userEmail = row.getOrThrow("user_email"),
                certificateId = row.getOrThrow("certificate_id"),
                transactionStatus = TransactionStatus.valueOf(row.getOrThrow("transaction_status")),
            )
        }
        .first()

    fun updateTransactionStatus(
        transactionId: UUID,
        transactionStatus: String,
    ): Mono<Void> = databaseClient.sql(
        """
            UPDATE certificates_transactions SET transaction_status = :transaction_status 
                WHERE transaction_id = :transaction_id
        """.trimIndent()
    )
        .bind("transaction_status", transactionStatus)
        .bind("transaction_id", transactionId)
        .then()
}