package ru.itmo.dao

import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import ru.itmo.model.response.UserCertificateInfoResponse
import ru.itmo.util.getNullable
import ru.itmo.util.getOrThrow
import java.time.Instant
import java.util.UUID

@Component
class UserCertificatesDao(
    private val databaseClient: DatabaseClient,
) {

    fun addNewCertificateToUser(
        userLogin: String,
        certificateId: UUID,
        expiresAt: Instant,
        isActive: Boolean = true,
    ): Mono<Void> = databaseClient
        .sql("INSERT INTO certificates_to_users (certificate_id, user_login, expires_at, is_active) VALUES (:certificate_id, :user_login, :expires_at, :is_active)")
        .bind("certificate_id", certificateId)
        .bind("user_login", userLogin)
        .bind("expires_at", expiresAt)
        .bind("is_active", isActive)
        .then()

    fun getUserAvailableCertificates(userLogin: String): Flux<UserCertificateInfoResponse> = databaseClient
        .sql(
            """
                SELECT c.id, c.title, ctu.expires_at, c.description FROM certificates_to_users ctu
                    JOIN certificates c on ctu.certificate_id = c.id
                    WHERE ctu.user_login = :login AND ctu.is_active AND ctu.expires_at > now()
            """.trimIndent()
        )
        .bind("login", userLogin)
        .map { row, _ ->
            UserCertificateInfoResponse(
                certificateId = row.getOrThrow("id"),
                title = row.getOrThrow("title"),
                expirationDate = row.getOrThrow("expires_at"),
                description = row.getNullable("description"),
            )
        }
        .all()

    fun getCertificateById(certificateId: UUID, userLogin: String): Mono<UserCertificateInfoResponse> = databaseClient
        .sql(
            """
                SELECT c.id, c.title, ctu.expires_at, c.description FROM certificates_to_users ctu
                    JOIN certificates c on ctu.certificate_id = c.id
                    WHERE c.id = :certificate_id AND ctu.user_login = :login AND ctu.is_active AND ctu.expires_at > now()
            """.trimIndent()
        )
        .bind("certificate_id", certificateId)
        .bind("login", userLogin)
        .map { row, _ ->
            UserCertificateInfoResponse(
                certificateId = row.getOrThrow("id"),
                title = row.getOrThrow("title"),
                expirationDate = row.getOrThrow("expires_at"),
                description = row.getOrThrow("description"),
            )
        }
        .first()

    fun checkUserCertificateOwnership(certificateId: UUID, userLogin: String): Mono<Void> = databaseClient
        .sql(
            """
                SELECT * FROM certificates_to_users ctu
                    JOIN certificates c on ctu.certificate_id = c.id
                    WHERE c.id = :certificate_id AND ctu.user_login = :login AND ctu.is_active AND ctu.expires_at > now()
            """.trimIndent()
        )
        .bind("certificate_id", certificateId)
        .bind("login", userLogin)
        .fetch()
        .one()
        .switchIfEmpty {
            Mono.error(NoSuchElementException("Certificate $certificateId not fot for user $userLogin"))
        }
        .then()
}
