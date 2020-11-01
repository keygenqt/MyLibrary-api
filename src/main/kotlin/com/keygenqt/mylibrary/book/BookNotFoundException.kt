package com.keygenqt.mylibrary.book

internal class BookNotFoundException(id: Long) : RuntimeException("Could not find employee $id")