package com.keygenqt.mylibrary.base.error

import org.springframework.http.*
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.*
import org.springframework.web.servlet.mvc.method.annotation.*
import java.io.*
import java.time.*

@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [
        ArithmeticException::class,
        IllegalArgumentException::class,
        ResponseStatusException::class
    ])
    @Throws(IOException::class)
    fun springHandle(ex: Exception): ResponseEntity<ErrorResponse> {
        var status = BAD_REQUEST.value()
        var error = BAD_REQUEST
        var message = ex.message ?: "Bad Request"
        ex.message?.let { value ->
            if (value.contains("404")) {
                status = NOT_FOUND.value()
                error = NOT_FOUND
                message = value.split("\"").toList().lastOrNull { it.isNotBlank() } ?: "Not found"
            }
        }
        val errorResponse = ErrorResponse(
            datetime = LocalDateTime.now(),
            status = status,
            error = error,
            message = message
        )
        return ResponseEntity(errorResponse, errorResponse.error)
    }
}