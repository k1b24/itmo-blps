package ru.itmo.model.event

import java.util.UUID

data class CertificateBoughtEvent(
    val login: String,
    val certificateId: UUID,
)