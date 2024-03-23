package ru.itmo.controller

import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.model.request.UserCardInfo
import ru.itmo.model.response.UserCertificateInfoResponse
import ru.itmo.service.UserCertificatesService
import java.util.UUID

@RestController
@RequestMapping("/v1")
class UserCertificatesController(
    private val userCertificatesService: UserCertificatesService,
) {
    @PostMapping("/user/certificates/{certificate-id}", produces = [MediaType.IMAGE_PNG_VALUE])
    fun addNewCertificateToUser(
        @PathVariable("certificate-id") certificateId: UUID,
        @RequestBody userCardInfo: UserCardInfo,
        authentication: Authentication,
    ): Mono<Void> = userCertificatesService
        .registerCertificateToUserAndGenerateQrCode(certificateId, userCardInfo, authentication)

    @GetMapping("/user/certificates")
    fun getAvailableCertificates(
        authentication: Authentication,
    ): Flux<UserCertificateInfoResponse> = userCertificatesService.getAvailableCertificates(authentication)

    @GetMapping("/user/certificates/{certificate-id}", produces = [MediaType.IMAGE_PNG_VALUE])
    fun getCertificateById(
        @PathVariable("certificate-id") certificateId: UUID,
        authentication: Authentication,
    ): Mono<ByteArray> = userCertificatesService.getCertificateById(certificateId, authentication)
}