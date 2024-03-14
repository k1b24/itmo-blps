package ru.itmo.lab1.security

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import ru.itmo.lab1.dao.UsersDao

@Component
class UserAuthenticationManager(
    private val usersDao: UsersDao,
    private val passwordEncoder: PasswordEncoder
) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> =
        usersDao.findUserPassword(authentication.name)
            .map { passwordFromDb ->
                if (!passwordEncoder.matches(authentication.credentials as String, passwordFromDb)) {
                    throw BadCredentialsException("Password is incorrect")
                }
                UserAuthentication(authentication.name)
            }
}