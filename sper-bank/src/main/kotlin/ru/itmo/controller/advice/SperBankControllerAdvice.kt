//package ru.itmo.controller.advice
//
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.ControllerAdvice
//import org.springframework.web.bind.annotation.ExceptionHandler
//import ru.itmo.model.exception.InvalidCardInfoException
//import ru.itmo.sper.bank.model.SperBankErrorResponse
//
//@ControllerAdvice
//class SperBankControllerAdvice {
//
//    @ExceptionHandler
//    fun exceptionHandler(exception: Exception): ResponseEntity<SperBankErrorResponse> =
//        ResponseEntity.internalServerError()
//            .body(
//                SperBankErrorResponse(
//                    message = "An unexpected exception occurred during processing of request",
//                    status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                )
//            )
//
//    @ExceptionHandler
//    fun invalidCardInfoExceptionHandler(exception: InvalidCardInfoException): ResponseEntity<SperBankErrorResponse> =
//        ResponseEntity.status(HttpStatus.BAD_REQUEST)
//            .body(
//                SperBankErrorResponse(
//                    message = "Invalid card info",
//                    status = HttpStatus.BAD_REQUEST.value(),
//                )
//            )
//}