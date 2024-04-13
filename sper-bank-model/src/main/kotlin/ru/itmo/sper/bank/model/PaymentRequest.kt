package ru.itmo.sper.bank.model

data class PaymentRequest(
    val creditCardNumber: String,
    val expirationDate: String,
    val cvvCode: String,
    val amount: Float,
)
