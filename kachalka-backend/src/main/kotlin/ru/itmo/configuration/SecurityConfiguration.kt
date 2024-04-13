package ru.itmo.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler
import org.springframework.security.web.server.authentication.ServerHttpBasicAuthenticationConverter
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import ru.itmo.model.UserPermission
import ru.itmo.security.BearerAuthenticationManager
import ru.itmo.security.JwtAuthenticationConverter
import ru.itmo.service.JwtTokenService

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration(
    private val securityProperties: SecurityProperties
) {

    @Bean
    fun springSecurityFilterChain(
        http: ServerHttpSecurity,
        basicAuthFilter: AuthenticationWebFilter,
        bearerTokenAuthFilter: AuthenticationWebFilter,
    ): SecurityWebFilterChain =
        http {
            authorizeExchange {
                authorize("/register", permitAll)
                authorize("/v1/validator", permitAll)
                authorize("/transactions/**", permitAll)
//                authorize("/v1/**", authenticated)
                authorize("/v1/login", authenticated)
                authorize("/v1/user/**", hasAuthority(UserPermission.BUY_CERTIFICATES.name))
                authorize("/v1/certificates/**", hasAuthority(UserPermission.EDIT_CERTIFICATES.name))
                authorize("/v1/moderators/**", hasAuthority(UserPermission.GRANT_MODERATOR_PERMISSIONS.name))
            }

            cors {  }
            csrf { disable() }

            addFilterAt(basicAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            addFilterAt(bearerTokenAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        }

    @Bean
    fun basicAuthFilter(basicAuthenticationManager: ReactiveAuthenticationManager): AuthenticationWebFilter =
        AuthenticationWebFilter(basicAuthenticationManager)
            .apply {
                setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/v1/login"))
                setServerAuthenticationConverter(ServerHttpBasicAuthenticationConverter())
                setAuthenticationFailureHandler(
                    ServerAuthenticationEntryPointFailureHandler(HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
                )
            }

    @Bean
    fun bearerTokenAuthFilter(tokenService: JwtTokenService): AuthenticationWebFilter =
        AuthenticationWebFilter(BearerAuthenticationManager(tokenService))
            .apply {
                setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/v1/**"))
                setServerAuthenticationConverter(JwtAuthenticationConverter())
                setAuthenticationFailureHandler(
                    ServerAuthenticationEntryPointFailureHandler(HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
                )
            }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder(4)
}