package ru.itmo.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import ru.itmo.dao.CertificatesDao
import ru.itmo.dao.UserCertificatesDao
import ru.itmo.dao.UsersDao
import ru.itmo.exception.PaymentProcessingException
import ru.itmo.exception.UserAlreadyHasSuchCertificateException
import ru.itmo.model.UserCertificateInfo
import ru.itmo.model.request.UserCardInfo
import ru.itmo.model.response.UserCertificateInfoResponse
import ru.itmo.service.qr.QrCodeGeneratorService
import ru.itmo.sper.bank.model.KachalkaPaymentRequest
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.*

@Service
class UserCertificatesService(
    private val userCertificatesDao: UserCertificatesDao,
    private val certificatesDao: CertificatesDao,
    private val objectMapper: ObjectMapper,
    private val qrCodeGeneratorService: QrCodeGeneratorService,
    private val sperBankWebClient: WebClient,
    private val usersDao: UsersDao,
) {

    fun registerCertificateToUserAndGenerateQrCode(
        certificateId: UUID,
        userCardInfo: UserCardInfo,
        authentication: Authentication,
    ): Mono<Void> =
        usersDao.findUserByLogin(authentication.name)
            .flatMap { userInfo ->
                userCertificatesDao.getUserAvailableCertificates(userInfo.login)
                    .collectList()
                    .flatMap { userCertificates ->
                        if (userCertificates.map { it.certificateId }.contains(certificateId))
                            Mono.error(UserAlreadyHasSuchCertificateException("User ${userInfo.login} already has certificate with id $certificateId"))
                        else Mono.just(userInfo)
                    }
            }
            .flatMap { certificatesDao.getCertificateById(certificateId) }
            .flatMap { certificate -> sendPaymentRequest(certificate.price, userCardInfo) }
            .then()
//            .flatMap { certificate ->
//                userCertificatesDao.addNewCertificateToUser(
//                    authentication.name,
//                    certificate.id,
//                    Instant.now().plus(certificate.duration).toTheEndOfTheDay()
//                )
//            }
//            .then(
//                qrCodeGeneratorService
//                    .generateQrCode(objectMapper.writeValueAsString(UserCertificateInfo(authentication.name, certificateId)))
//            )

    fun getAvailableCertificates(authentication: Authentication): Flux<UserCertificateInfoResponse> =
        userCertificatesDao.getUserAvailableCertificates(authentication.name)

    fun getCertificateById(certificateId: UUID, authentication: Authentication): Mono<ByteArray> =
        userCertificatesDao.getCertificateById(certificateId, authentication.name).switchIfEmpty {
            Mono.error(NoSuchElementException("No such available certificates by specified id = $certificateId"))
        }
            .then(
                qrCodeGeneratorService
                    .generateQrCode(objectMapper.writeValueAsString(UserCertificateInfo(authentication.name, certificateId)))
            )

    private fun Instant.toTheEndOfTheDay() = this.atOffset(ZoneOffset.UTC)
        .plusDays(1).with(LocalTime.of(23, 59, 59, this.nano))
        .toInstant()

    private fun sendPaymentRequest(amount: Float, userCardInfo: UserCardInfo): Mono<ResponseEntity<Void>> =
        sperBankWebClient.post()
            .uri("/kachalka-payment")
            .bodyValue(
                KachalkaPaymentRequest(
                    creditCardNumber = userCardInfo.creditCardNumber,
                    expirationDate = userCardInfo.expirationDate,
                    cvvCode = userCardInfo.cvvCode,
                    amount = amount,
                )
            )
            .retrieve()
            .onStatus(HttpStatus::isError) {
                Mono.error(PaymentProcessingException("An error occurred while payment processing"))
            }
            .toBodilessEntity()
}
