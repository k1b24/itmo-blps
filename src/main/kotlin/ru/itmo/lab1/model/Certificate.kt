package ru.itmo.lab1.model

import java.time.Duration
import java.time.Instant
import java.util.*

data class Certificate(
    val id: UUID,
    val title: String,
    val price: Float,
    val duration: Duration,
    val startAt: Instant,
    val endAt: Instant,
    val description: String?,
    val picture: String?,
)
