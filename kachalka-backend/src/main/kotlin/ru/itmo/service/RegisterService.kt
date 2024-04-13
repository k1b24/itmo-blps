package ru.itmo.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.itmo.dao.UsersDao
import ru.itmo.dao.UsersRolesDao
import ru.itmo.model.UserRole
import ru.itmo.model.request.RegisterNewUserRequest

@Service
class RegisterService(
    private val usersDao: UsersDao,
    private val passwordEncoder: PasswordEncoder,
    private val usersRolesDao: UsersRolesDao,
) {

    fun registerNewUser(registerNewUserRequest: RegisterNewUserRequest): Mono<Void> = usersDao
        .addNewUser(
            login = registerNewUserRequest.login,
            password = passwordEncoder.encode(registerNewUserRequest.password),
            fullname = registerNewUserRequest.fullname,
            phoneNumber = registerNewUserRequest.phoneNumber,
            email = registerNewUserRequest.email,
        )
        .then(usersRolesDao.saveRolesForUser(registerNewUserRequest.login, UserRole.USER.name))
}