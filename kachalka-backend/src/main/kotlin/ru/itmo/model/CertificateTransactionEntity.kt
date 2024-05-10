package ru.itmo.model

import ru.itmo.sper.bank.model.TransactionStatus
import java.util.UUID

data class CertificateTransactionEntity(
    val transactionId: UUID,
    val userLogin: String,
    val userEmail: String,
    val certificateId: UUID,
    val transactionStatus: TransactionStatus,
)