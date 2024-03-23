package ru.itmo.sper.bank.model

data class KachalkaPaymentRequest(
    val creditCardNumber: String,
    val expirationDate: String,
    val cvvCode: String,
    val amount: Float,
)
