package ru.itmo.qr.validator.service.model

import java.util.UUID

data class UserCertificateInfo(
    val login: String,
    val certificateId: UUID,
)