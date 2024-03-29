package ru.itmo.controller.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.itmo.exception.PaymentProcessingException
import ru.itmo.exception.UserAlreadyHasSuchCertificateException
import ru.itmo.model.response.ErrorResponse
import java.lang.Exception

@ControllerAdvice
class KachalkaBackendControllerAdvice {

    @ExceptionHandler
    fun exceptionHandler(exception: Exception): ResponseEntity<ErrorResponse> =
        ResponseEntity.internalServerError()
            .body(
                ErrorResponse(
                    message = "An unexpected exception occurred during processing of request",
                    status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                )
            )

    @ExceptionHandler
    fun paymentProcessingExceptionHandler(exception: PaymentProcessingException): ResponseEntity<ErrorResponse> =
        ResponseEntity.internalServerError()
            .body(
                ErrorResponse(
                    message = "Payment service is unavailable",
                    status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                )
            )

    @ExceptionHandler
    fun noSuchElementExceptionHandler(exception: NoSuchElementException): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse(
                    message = exception.message,
                    status = HttpStatus.NOT_FOUND.value(),
                )
            )

    @ExceptionHandler
    fun userAlreadyHasSuchCertificateExceptionHandler(exception: UserAlreadyHasSuchCertificateException): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                ErrorResponse(
                    message = exception.message,
                    status = HttpStatus.BAD_REQUEST.value(),
                )
            )
}