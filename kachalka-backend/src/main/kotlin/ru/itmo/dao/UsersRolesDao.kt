package ru.itmo.dao

import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import ru.itmo.util.getOrThrow

@Component
class UsersRolesDao(
    private val databaseClient: DatabaseClient,
) {

    fun findRolesByUserLogin(userLogin: String): Mono<List<String>> = databaseClient
        .sql(
            """
                SELECT role FROM users_roles WHERE user_login = :login
            """.trimIndent()
        )
        .bind("login", userLogin)
        .map { row, _ ->
            row.getOrThrow<String>("role")
        }
        .all()
        .collectList()

    fun saveRolesForUser(userLogin: String, role: String): Mono<Void> = databaseClient
        .sql(
            """
                INSERT INTO users_roles (user_login, role) VALUES (:login, :role)
            """.trimIndent()
        )
        .bind("login", userLogin)
        .bind("role", role)
        .then()
}