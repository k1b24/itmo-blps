package ru.itmo.security

import org.springframework.security.authentication.AbstractAuthenticationToken

class BearerAuthenticationToken(
    val token: String
) : AbstractAuthenticationToken(emptyList()) {

    override fun getCredentials(): Any = token

    override fun getPrincipal(): Any = token
}
