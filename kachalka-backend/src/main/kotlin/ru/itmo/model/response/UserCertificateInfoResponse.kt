package ru.itmo.model.response

import java.time.Instant
import java.util.*

data class UserCertificateInfoResponse(
    val certificateId: UUID,
    val title: String,
    val expirationDate: Instant,
    val description: String,
)