package com.keygenqt.mylibrary.extensions

import org.springframework.http.*
import org.springframework.http.HttpStatus.*
import org.springframework.validation.*
import java.sql.*

fun BindingResult.getErrorFormat(): ResponseEntity<Any> {
    return ResponseEntity(hashMapOf<String, Any>("errors" to this.fieldErrors).apply {
        put("status", UNPROCESSABLE_ENTITY.value())
        put("error", "Validate")
        put("message", "Validation failed")
        put("timestamp", Timestamp(System.currentTimeMillis()))
    }, UNPROCESSABLE_ENTITY)
}

fun getSuccessFormat(message: String): ResponseEntity<Any> {
    return ResponseEntity(hashMapOf<String, Any>().apply {
        put("status", OK.value())
        put("message", message)
        put("timestamp", Timestamp(System.currentTimeMillis()))
    }, OK)
}