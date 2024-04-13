package ru.itmo.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class BearerTokenUserAuthentication(
    private val username: String,
    private val permissions: Collection<GrantedAuthority>,
    private val token: String,
) : Authentication {

    override fun getName(): String = username

    override fun getAuthorities(): Collection<GrantedAuthority> = permissions

    override fun getCredentials(): Any? = null

    override fun getDetails(): Any? = null

    override fun getPrincipal(): Any = username

    override fun isAuthenticated(): Boolean = true

    override fun setAuthenticated(isAuthenticated: Boolean) {}

    fun getToken(): String = token
}
