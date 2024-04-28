package ru.itmo.security

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

@Component
class KeycloakGrantedAuthoritiesConverter : Converter<Jwt, Collection<GrantedAuthority>> {

    override fun convert(jwt: Jwt): Collection<GrantedAuthority> =
        realmRoles(jwt).map { SimpleGrantedAuthority(it) }

    @Suppress("UNCHECKED_CAST")
    private fun realmRoles(jwt: Jwt): List<String> {
        val claimsMap = jwt.getClaimAsMap(CLAIM_REALM_ACCESS)
        val roles = claimsMap[ROLES] ?: return emptyList()
        // TODO кастовать по элементам
        return (roles as? List<String>) ?: emptyList()
    }

    companion object {
        private const val ROLES = "roles"
        private const val CLAIM_REALM_ACCESS = "realm_access"
    }
}