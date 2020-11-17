package com.keygenqt.mylibrary.base.error

import org.springframework.http.*
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.*
import java.io.*
import java.time.*

@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [
        Exception::class
    ])
    @Throws(IOException::class)
    fun springHandle(ex: Exception): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            datetime = LocalDateTime.now(),
            status = BAD_REQUEST.value(),
            error = BAD_REQUEST,
            message = ex.message ?: "Bad Request"
        )
        return ResponseEntity(error, error.error)
    }
}