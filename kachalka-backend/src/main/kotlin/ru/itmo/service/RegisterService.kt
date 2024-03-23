package ru.itmo.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.itmo.dao.UsersDao
import ru.itmo.model.request.RegisterNewUserRequest

@Service
class RegisterService(
    private val usersDao: ru.itmo.dao.UsersDao,
    private val passwordEncoder: PasswordEncoder,
) {

    fun registerNewUser(registerNewUserRequest: ru.itmo.model.request.RegisterNewUserRequest): Mono<Void> = usersDao
        .addNewUser(
            login = registerNewUserRequest.login,
            password = passwordEncoder.encode(registerNewUserRequest.password),
            fullname = registerNewUserRequest.fullname,
            phoneNumber = registerNewUserRequest.phoneNumber,
            email = registerNewUserRequest.email,
        )
}