package ru.itmo.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.itmo.dao.UsersInfoDao
import ru.itmo.sper.bank.model.CreateUserRequest
import java.util.UUID

@Service
class UserService(
    private val usersInfoDao: UsersInfoDao,
) {
    fun createUser(
        request: CreateUserRequest
    ): Mono<UUID> {
        return usersInfoDao.createUser(
            request.creditCardNumber,
            request.expirationDate,
            request.cvvCode,
            request.balance,
        )
    }
}