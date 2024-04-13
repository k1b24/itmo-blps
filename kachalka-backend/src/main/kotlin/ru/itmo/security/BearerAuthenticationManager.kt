package ru.itmo.security

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import reactor.core.publisher.Mono
import ru.itmo.service.JwtTokenService

class BearerAuthenticationManager(
    private val tokenService: JwtTokenService,
): ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> = Mono.just(authentication)
        .flatMap {
            if (authentication is BearerAuthenticationToken) {
                Mono.just(tokenService.verifyTokenAndGetAuthentication(authentication.token))
            } else {
                Mono.empty()
            }
        }
}