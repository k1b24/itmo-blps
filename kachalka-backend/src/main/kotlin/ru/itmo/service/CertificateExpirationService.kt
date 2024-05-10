package ru.itmo.service

import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import reactor.core.scheduler.Schedulers
import ru.itmo.dao.UserCertificatesDao

@Service
@Profile("cron")
class CertificateExpirationService(
    private val userCertificatesDao: UserCertificatesDao,
) {

    @Scheduled(fixedDelay = 1000)
    fun invalidateCertificates() {
        userCertificatesDao.invalidateUsersCertificates().subscribeOn(Schedulers.parallel()).subscribe()
    }

}
