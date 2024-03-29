package ru.itmo.controller

import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.model.Certificate
import ru.itmo.model.request.AddCertificateRequest
import ru.itmo.service.CertificateService
import java.util.UUID

@RestController
@RequestMapping("/v1/certificates")
class CertificatesController(
    val certificateService: CertificateService,
) {

    @GetMapping
    fun getCertificates(): Flux<Certificate> = certificateService.getCertificates()

    @PostMapping
    fun postCertificate(
        @RequestBody certificate: AddCertificateRequest,
    ): Mono<Void> = certificateService.addNewCertificate(certificate)

    @DeleteMapping("/{certificate-id}")
    fun deleteCertificate(
        @PathVariable("certificate-id") certificateId: UUID,
    ): Mono<Void> = certificateService.deleteCertificateById(certificateId)
}
