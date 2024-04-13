package ru.itmo.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import ru.itmo.dao.UserCertificatesDao
import ru.itmo.model.UserCertificateInfo

@Service
class ValidatorService(
    private val userCertificatesDao: UserCertificatesDao,
) {
    @Transactional(readOnly = true)
    fun validateUserCertificateInfo(userCertificateInfo: UserCertificateInfo): Mono<Void> =
        userCertificatesDao.checkUserCertificateOwnership(userCertificateInfo.certificateId, userCertificateInfo.login)
}