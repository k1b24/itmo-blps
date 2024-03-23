package ru.itmo.qr.validator.service.controller.advice

import com.google.zxing.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.itmo.qr.validator.service.model.ErrorResponse
import ru.itmo.qr.validator.service.model.exception.CertificateNotFoundException
import ru.itmo.qr.validator.service.model.exception.KachalkaRequestException
import java.lang.Exception


@ControllerAdvice
class QrValidatorServiceControllerAdvice {

    @ExceptionHandler
    fun handleZxingNotFoundException(exception: NotFoundException): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    message = "Qr code image is not correct, data cannot be deserialized",
                    statusCode = HttpStatus.BAD_REQUEST.value(),
                )
            )

    @ExceptionHandler
    fun handleException(exception: Exception): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    message = "Something went wrong, e = $exception",
                    statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                )
            )

    @ExceptionHandler
    fun handleCertificateNotFoundException(exception: CertificateNotFoundException): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse(
                    message = "Such certificate is not found",
                    statusCode = HttpStatus.NOT_FOUND.value(),
                )
            )

    @ExceptionHandler
    fun handleKachalkaRequestException(exception: KachalkaRequestException): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    message = "An error occurred while checking certificate at remote service",
                    statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                )
            )
}