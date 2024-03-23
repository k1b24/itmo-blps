package ru.itmo.model.request

data class UserCardInfo(
    val creditCardNumber: String,
    val expirationDate: String,
    val cvvCode: String,
)
