package com.keygenqt.mylibrary.books

import org.springframework.http.*
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Order")
internal class BookNotFoundException(id: Long) : RuntimeException("Could not find employee $id")

@ResponseStatus(code = NOT_FOUND, reason = "Actor Not Found")
class ActorNotFoundException : Exception() {
}