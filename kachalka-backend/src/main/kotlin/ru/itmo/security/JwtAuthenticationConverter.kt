package ru.itmo.security

import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class JwtAuthenticationConverter : ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange): Mono<Authentication> =
        Mono.just(exchange).flatMap {
            val token = exchange.request.headers["Authorization"]?.firstOrNull()
            if (!token.isNullOrEmpty() && token.startsWith("Bearer ")) {
                Mono.just(BearerAuthenticationToken(token.substring("Bearer ".length)))
            } else {
                Mono.empty()
            }
        }
}