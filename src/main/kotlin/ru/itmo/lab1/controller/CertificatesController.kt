package ru.itmo.lab1.controller

import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.lab1.model.Certificate
import ru.itmo.lab1.model.request.AddCertificateRequest
import ru.itmo.lab1.service.CertificateService
import java.util.UUID

@RestController
@RequestMapping("v1/certificates")
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
        @PathVariable certificateId: UUID,
    ): Mono<Void> = certificateService.deleteCertificateById(certificateId)
}
