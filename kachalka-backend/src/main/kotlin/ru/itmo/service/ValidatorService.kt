package ru.itmo.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.itmo.dao.UserCertificatesDao
import ru.itmo.model.UserCertificateInfo

@Service
class ValidatorService(
    private val userCertificatesDao: UserCertificatesDao,
) {

    fun validateUserCertificateInfo(userCertificateInfo: UserCertificateInfo): Mono<Void> =
        userCertificatesDao.checkUserCertificateOwnership(userCertificateInfo.certificateId, userCertificateInfo.login)
}