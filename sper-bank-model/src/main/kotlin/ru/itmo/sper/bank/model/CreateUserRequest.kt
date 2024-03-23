package ru.itmo.sper.bank.model

data class CreateUserRequest(
    val creditCardNumber: String,
    val expirationDate: String,
    val cvvCode: String,
    val balance: Float,
)