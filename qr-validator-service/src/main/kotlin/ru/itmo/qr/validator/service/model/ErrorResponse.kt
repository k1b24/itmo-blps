package ru.itmo.qr.validator.service.model

data class ErrorResponse(
    val message: String,
    val statusCode: Int,
)