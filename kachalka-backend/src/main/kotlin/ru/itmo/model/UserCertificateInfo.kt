package ru.itmo.model

import java.util.UUID

data class UserCertificateInfo(
    val login: String,
    val certificateId: UUID,
)