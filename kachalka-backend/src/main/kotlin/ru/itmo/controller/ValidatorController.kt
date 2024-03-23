package ru.itmo.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.itmo.model.UserCertificateInfo
import ru.itmo.service.ValidatorService

@RestController
@RequestMapping("/v1/validator")
class ValidatorController(
    private val validatorService: ValidatorService,
) {

    @PostMapping
    fun validateUserCertificate(
        @RequestBody userCertificateInfo: UserCertificateInfo,
    ): Mono<Void> = validatorService.validateUserCertificateInfo(userCertificateInfo)

}