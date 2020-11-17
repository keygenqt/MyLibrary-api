package com.keygenqt.mylibrary.base.error

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.annotation.JsonFormat.*
import org.springframework.http.*
import java.time.*

class ErrorResponse(
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    val datetime: LocalDateTime,
    val status: Int,
    val error: HttpStatus,
    val message: String
)