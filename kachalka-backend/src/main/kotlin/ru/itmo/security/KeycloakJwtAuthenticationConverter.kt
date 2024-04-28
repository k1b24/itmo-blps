package ru.itmo.security

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.configuration.SecurityProperties
import ru.itmo.model.UserPermission
import ru.itmo.model.UserRole

@Component
class KeycloakJwtAuthenticationConverter(
    keycloakGrantedAuthoritiesConverter: KeycloakGrantedAuthoritiesConverter,
    val securityProperties: SecurityProperties,
) : Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private val jwtGrantedAuthoritiesConverter: Converter<Jwt, Flux<GrantedAuthority>> =
        ReactiveJwtGrantedAuthoritiesConverterAdapter(keycloakGrantedAuthoritiesConverter)

    override fun convert(jwt: Jwt): Mono<AbstractAuthenticationToken> {
        return jwtGrantedAuthoritiesConverter.convert(jwt)!!
            .collectList()
            .map { authorities ->
                authorities.flatMap {
                    if (UserRole.values().map { it.name }.contains(it.authority)) {
                        securityProperties.permissions.get(UserRole.valueOf(it.authority)) ?: emptyList()
                    } else {
                        emptyList()
                    }
                }
                    .map {
                        GrantedAuthority { it.name }
                    }
            }
            .map { authorities ->
                JwtAuthenticationToken(jwt, authorities, extractUsername(jwt))
            }
    }

    protected fun extractUsername(jwt: Jwt): String {
        return if (jwt.claims[USERNAME_CLAIM] != null) jwt.getClaimAsString(USERNAME_CLAIM) else jwt.subject
    }

    companion object {
        private const val USERNAME_CLAIM = "preferred_username"
    }
}