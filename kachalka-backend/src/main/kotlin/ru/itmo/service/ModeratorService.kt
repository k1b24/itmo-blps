package ru.itmo.service

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import ru.itmo.dao.UsersRolesDao
import ru.itmo.model.UserRole

@Component
class ModeratorService(
    private val usersRolesDao: UsersRolesDao
) {

    fun addModeratorRoleToUser(login: String): Mono<Void> = usersRolesDao
        .saveRolesForUser(login, UserRole.MODERATOR.name)

}