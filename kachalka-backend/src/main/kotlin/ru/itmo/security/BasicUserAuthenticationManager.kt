package ru.itmo.security

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import ru.itmo.configuration.SecurityProperties
import ru.itmo.dao.UsersDao
import ru.itmo.dao.UsersRolesDao
import ru.itmo.exception.UserNotFoundException
import ru.itmo.model.UserRole

@Component
class BasicUserAuthenticationManager(
    private val usersDao: UsersDao,
    private val passwordEncoder: PasswordEncoder,
    private val usersRolesDao: UsersRolesDao,
    private val securityProperties: SecurityProperties
) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> =
        usersDao.findUserPassword(authentication.name)
            .switchIfEmpty { throw BadCredentialsException("Login is incorrect") }
            .map { passwordFromDb ->
                if (!passwordEncoder.matches(authentication.credentials as String, passwordFromDb)) {
                    throw BadCredentialsException("Password is incorrect")
                }
                authentication.name
            }
            .flatMap { userName ->
                usersRolesDao.findRolesByUserLogin(userName)
            }
            .map { roles ->
                val permissions = roles
                    .flatMap {
                        securityProperties.permissions[UserRole.valueOf(it)] ?: throw Exception("An unexpected role retrieved from database")
                    }
                    .map {
                        GrantedAuthority { it.name }
                    }
                    .toMutableList()
                if (securityProperties.adminUsers.contains(authentication.name)) {
                    val adminPermissions = securityProperties.permissions[securityProperties.adminPermissionName]
                        ?: throw Exception("admin permissions not found in config")
                    adminPermissions.forEach { permissions.add(GrantedAuthority { it.name }) }
                }
                UserAuthentication(authentication.name, permissions)
            }
}