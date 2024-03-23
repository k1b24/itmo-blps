package ru.itmo.dao

import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.model.Certificate
import ru.itmo.util.bindNullable
import ru.itmo.util.getNullable
import ru.itmo.util.getOrThrow
import java.time.Duration
import java.time.Instant
import java.util.UUID

@Component
class CertificatesDao(
    val databaseClient: DatabaseClient,
) {

    fun getCertificates(): Flux<Certificate> = databaseClient
        .sql("SELECT * FROM certificates")
        .map { row, _ ->
            Certificate(
                id = row.getOrThrow("id"),
                title = row.getOrThrow("title"),
                price = row.getOrThrow("price"),
                duration = Duration.ofMillis(row.getOrThrow("duration")),
                startAt = row.getOrThrow("start_at"),
                endAt = row.getOrThrow("end_at"),
                description = row.getNullable("description"),
                picture = row.getNullable("picture"),
            )
        }
        .all()

    fun addCertificate(
        id: UUID,
        name: String,
        price: Float,
        duration: Duration,
        startAt: Instant,
        endAt: Instant,
        description: String? = null,
        pictureUrl: String? = null,
    ): Mono<Void> = databaseClient
        .sql("INSERT INTO certificates (id, title, price, duration, start_at, end_at, description, picture) VALUES (:id, :name, :price, :duration, :start_at, :end_at, :description, :picture_url)")
        .bind("id", id)
        .bind("name", name)
        .bind("price", price)
        .bind("duration", duration.toMillis())
        .bind("start_at", startAt)
        .bind("end_at", endAt)
        .bindNullable("description", description)
        .bindNullable("picture_url", pictureUrl)
        .then()

    fun getCertificateById(id: UUID): Mono<Certificate> = databaseClient
        .sql("SELECT * FROM certificates WHERE id = :id")
        .bind("id", id)
        .map { row, _ ->
            Certificate(
                id = row.getOrThrow("id"),
                title = row.getOrThrow("title"),
                price = row.getOrThrow("price"),
                duration = Duration.ofMillis(row.getOrThrow("duration")),
                startAt = row.getOrThrow("start_at"),
                endAt = row.getOrThrow("end_at"),
                description = row.getNullable("description"),
                picture = row.getNullable("picture"),
            )
        }
        .first()

    fun deleteCertificateById(certificateId: UUID): Mono<Void> = databaseClient
        .sql("DELETE FROM certificates WHERE id = :id")
        .bind("id", certificateId)
        .then()
}
