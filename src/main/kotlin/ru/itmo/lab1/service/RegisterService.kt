package ru.itmo.lab1.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.itmo.lab1.dao.UsersDao
import ru.itmo.lab1.model.request.RegisterNewUserRequest

@Service
class RegisterService(
    private val usersDao: UsersDao,
    private val passwordEncoder: PasswordEncoder,
) {

    fun registerNewUser(registerNewUserRequest: RegisterNewUserRequest): Mono<Void> = usersDao
        .addNewUser(
            login = registerNewUserRequest.login,
            password = passwordEncoder.encode(registerNewUserRequest.password),
            fullname = registerNewUserRequest.fullname,
            phoneNumber = registerNewUserRequest.phoneNumber,
            email = registerNewUserRequest.email,
        )
}