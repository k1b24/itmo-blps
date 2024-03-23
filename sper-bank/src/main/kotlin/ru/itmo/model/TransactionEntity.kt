package ru.itmo.model

import ru.itmo.sper.bank.model.TransactionStatus
import java.util.UUID

data class TransactionEntity (
    val id: UUID,
    val userId: UUID,
    val status: TransactionStatus,
    val amount: Float,
)