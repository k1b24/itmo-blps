package ru.itmo.lab1.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.lab1.model.response.UserCertificateInfoResponse
import ru.itmo.lab1.service.UserCertificatesService
import java.util.UUID

@RestController
@RequestMapping("/v1")
class UserCertificatesController(
    private val userCertificatesService: UserCertificatesService,
) {

    @PostMapping("/user/certificates/{certificate-id}", produces = [MediaType.IMAGE_PNG_VALUE])
    fun addNewCertificateToUser(
        @PathVariable("certificate-id") certificateId: UUID,
        authentication: Authentication,
    ): Mono<ByteArray> = userCertificatesService
        .registerCertificateToUserAndGenerateQrCode(certificateId, authentication)

    @GetMapping("/user/certificates")
    fun getAvailableCertificates(
        authentication: Authentication,
    ): Flux<UserCertificateInfoResponse> = userCertificatesService.getAvailableCertificates(authentication)
}