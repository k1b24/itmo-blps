package ru.itmo.lab1.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.lab1.dao.CertificatesDao
import ru.itmo.lab1.dao.UserCertificatesDao
import ru.itmo.lab1.dao.UsersDao
import ru.itmo.lab1.model.UserCertificateInfo
import ru.itmo.lab1.model.response.UserCertificateInfoResponse
import ru.itmo.lab1.service.qr.QrCodeGeneratorService
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.*

@Service
class UserCertificatesService(
    private val userCertificatesDao: UserCertificatesDao,
    private val usersDao: UsersDao,
    private val certificatesDao: CertificatesDao,
    private val objectMapper: ObjectMapper,
    private val qrCodeGeneratorService: QrCodeGeneratorService,
) {

    fun registerCertificateToUserAndGenerateQrCode(
        certificateId: UUID,
        authentication: Authentication,
    ): Mono<ByteArray> =
        certificatesDao
            .getCertificateById(certificateId)
            .flatMap { certificate ->
                userCertificatesDao.addNewCertificateToUser(
                    authentication.name,
                    certificate.id,
                    Instant.now().plus(certificate.duration).toTheEndOfTheDay()
                )
            }
            .then(qrCodeGeneratorService.generateQrCode(objectMapper.writeValueAsString(UserCertificateInfo(authentication.name, certificateId))))

    fun getAvailableCertificates(authentication: Authentication): Flux<UserCertificateInfoResponse> =
        userCertificatesDao.getUserAvailableCertificates(authentication.name)

    private fun Instant.toTheEndOfTheDay() = this.atOffset(ZoneOffset.UTC)
        .plusDays(1).with(LocalTime.of(23,59,59, this.nano))
        .toInstant()
}
