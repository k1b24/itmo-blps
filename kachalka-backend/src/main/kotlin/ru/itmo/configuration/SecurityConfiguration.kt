package ru.itmo.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.GET
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import ru.itmo.model.UserPermission
import ru.itmo.security.KeycloakJwtAuthenticationConverter

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {

    @Bean
    fun springSecurityFilterChain(
        http: ServerHttpSecurity,
        converter: KeycloakJwtAuthenticationConverter,
    ): SecurityWebFilterChain =
        http {
            authorizeExchange {
                authorize("/v1/validator", permitAll)
                authorize("/transactions/**", permitAll)
                authorize("/v1/user/**", hasAuthority(UserPermission.BUY_CERTIFICATES.name))
                authorize(ServerWebExchangeMatchers.pathMatchers(GET, "/v1/certificates"), hasAuthority(UserPermission.BUY_CERTIFICATES.name))
                authorize("/v1/certificates/**", hasAuthority(UserPermission.EDIT_CERTIFICATES.name))
                authorize("/v1/moderators/**", hasAuthority(UserPermission.GRANT_MODERATOR_PERMISSIONS.name))
            }

            oauth2ResourceServer {
                jwt {
                    jwtAuthenticationConverter = converter
                }
            }
            oauth2Login {  }

            cors {  }
            csrf { disable() }
        }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder(4)
}