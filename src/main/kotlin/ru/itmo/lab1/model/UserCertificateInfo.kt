package ru.itmo.lab1.model

import java.util.UUID

data class UserCertificateInfo(
    val login: String,
    val certificateId: UUID,
)