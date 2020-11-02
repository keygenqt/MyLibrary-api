package com.keygenqt.mylibrary.books

internal class BookNotFoundException(id: Long) : RuntimeException("Could not find employee $id")