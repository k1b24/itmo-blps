package ru.itmo.configuration

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service

@Service
class MetricsService(
    meterRegistry: MeterRegistry,
) {

    private val successCertificatePurchaseCounter = Counter
        .builder("success_certificate_purchase")
        .register(meterRegistry)

    private val failedCertificatePurchaserCounter = Counter
        .builder("failed_certificate_purchaser")
        .register(meterRegistry)

    fun countSuccessCertificatePurchase() {
        successCertificatePurchaseCounter.increment()
    }

    fun countFailedCertificatePurchase() {
        failedCertificatePurchaserCounter.increment()
    }
}