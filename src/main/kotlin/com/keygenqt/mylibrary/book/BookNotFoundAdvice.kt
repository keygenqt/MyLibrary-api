package com.keygenqt.mylibrary.book

import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*

@ControllerAdvice
internal class BookNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(BookNotFoundException::class)
    @ResponseStatus(NOT_FOUND)
    fun employeeNotFoundHandler(
        ex: BookNotFoundException): String {
        return ex.message ?: "Not found"
    }
}