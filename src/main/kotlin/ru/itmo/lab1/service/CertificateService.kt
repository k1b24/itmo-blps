package ru.itmo.lab1.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.lab1.dao.CertificatesDao
import ru.itmo.lab1.model.Certificate
import ru.itmo.lab1.model.request.AddCertificateRequest
import java.util.UUID

@Service
class CertificateService(
    val certificatesDao: CertificatesDao,
) {

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