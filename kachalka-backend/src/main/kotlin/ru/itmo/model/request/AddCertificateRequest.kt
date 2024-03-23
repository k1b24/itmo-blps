package ru.itmo.model.request

import java.time.Duration
import java.time.Instant

data class AddCertificateRequest(
    val title: String,
    val price: Float,
    val duration: Duration,
    val startAt: Instant,
    val endAt: Instant,
    val description: String?,
    val picture: String?,
)
