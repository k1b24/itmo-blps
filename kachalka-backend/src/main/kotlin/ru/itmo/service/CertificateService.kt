package ru.itmo.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.dao.CertificatesDao
import ru.itmo.model.Certificate
import ru.itmo.model.request.AddCertificateRequest
import java.util.UUID

@Service
class CertificateService(
    val certificatesDao: CertificatesDao,
) {
    @Transactional(readOnly = true)
    fun getCertificates(): Flux<Certificate> = certificatesDao.getCertificates()

    fun addNewCertificate(certificate: AddCertificateRequest): Mono<Void> =
        certificatesDao.addCertificate(
            id = UUID.randomUUID(),
            name = certificate.title,
            price = certificate.price,
            duration = certificate.duration,
            startAt = certificate.startAt,
            endAt = certificate.endAt,
            description = certificate.description,
            pictureUrl = certificate.picture
        )

    fun deleteCertificateById(certificateId: UUID): Mono<Void> =
        certificatesDao.deleteCertificateById(certificateId)
}