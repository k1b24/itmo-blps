package ru.itmo.model.response

data class ErrorResponse(
    val message: String?,
    val status: Int?,
)
