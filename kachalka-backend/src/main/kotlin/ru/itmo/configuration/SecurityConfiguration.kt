package ru.itmo.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler
import org.springframework.security.web.server.authentication.ServerHttpBasicAuthenticationConverter
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity, basicAuthFilter: AuthenticationWebFilter): SecurityWebFilterChain =
        http {
            authorizeExchange {
                authorize("/register", permitAll)
                authorize("/v1/validator", permitAll)
                authorize("/transactions/**", permitAll)
                authorize("/v1/**", authenticated)
            }

            cors {  }
            csrf { disable() }

            addFilterAt(basicAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        }

    @Bean
    fun basicAuthFilter(basicAuthenticationManager: ReactiveAuthenticationManager): AuthenticationWebFilter =
        AuthenticationWebFilter(basicAuthenticationManager)
            .apply {
                setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/v1/**"))
                setServerAuthenticationConverter(ServerHttpBasicAuthenticationConverter())
                setAuthenticationFailureHandler(
                    ServerAuthenticationEntryPointFailureHandler(HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
                )
            }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder(4)
}