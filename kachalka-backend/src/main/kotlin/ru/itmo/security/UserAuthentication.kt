package ru.itmo.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

data class UserAuthentication(
    private val name: String,
) : Authentication {

    override fun getName(): String = name

    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()

    override fun getCredentials(): Any? = null

    override fun getDetails(): Any? = null

    override fun getPrincipal(): Any = name

    override fun isAuthenticated(): Boolean = true

    override fun setAuthenticated(isAuthenticated: Boolean) {}
}