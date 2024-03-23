package ru.itmo.dao

import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import ru.itmo.model.KachalkaUserInfo
import ru.itmo.util.getOrThrow
import java.util.UUID

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

    fun findUserByLogin(login: String): Mono<KachalkaUserInfo> = databaseClient
        .sql("SELECT login, fullname, phone_number, email FROM users WHERE login = :login")
        .bind("login", login)
        .map { row, _ ->
            KachalkaUserInfo(
                login = row.getOrThrow("login"),
                fullname = row.getOrThrow("fullname"),
                phoneNumber = row.getOrThrow("phone_number"),
                email = row.getOrThrow("email"),
            )
        }
        .first()
}