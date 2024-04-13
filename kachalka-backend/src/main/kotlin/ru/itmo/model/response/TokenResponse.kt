package ru.itmo.model.response

data class TokenResponse(
    val token: String,
    val ttl: Long,
)