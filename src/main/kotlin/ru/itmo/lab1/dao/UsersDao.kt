package ru.itmo.lab1.dao

import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import ru.itmo.lab1.util.getOrThrow
import java.util.UUID
import kotlin.math.log

@Component
class UsersDao(
    private val databaseClient: DatabaseClient,
) {

    fun findUserPassword(login: String): Mono<String> = databaseClient
        .sql("SELECT password FROM users WHERE login = :login")
        .bind("login", login)
        .map { row, _ ->
            row.getOrThrow<String>("password")
        }
        .first()

    fun addNewUser(
        login: String,
        password: String,
        fullname: String,
        phoneNumber: String,
        email: String,
    ): Mono<Void> = databaseClient
        .sql("INSERT INTO users (login, password, fullname, phone_number, email) VALUES (:login, :password, :fullname, :phone_number, :email)")
        .bind("login", login)
        .bind("password", password)
        .bind("fullname", fullname)
        .bind("phone_number", phoneNumber)
        .bind("email", email)
        .then()

    fun findUserIdByLogin(login: String): Mono<UUID> = databaseClient
        .sql("SELECT id FROM users WHERE login = :login")
        .bind("login", login)
        .map { row, _ ->
            row.getOrThrow<UUID>("id")
        }
        .first()
}