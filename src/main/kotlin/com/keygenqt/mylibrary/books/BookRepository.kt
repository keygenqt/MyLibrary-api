package com.keygenqt.mylibrary.books

import org.springframework.data.domain.*
import org.springframework.data.jpa.repository.*
import org.springframework.data.repository.*

internal interface BookRepository : PagingAndSortingRepository<Book, Long>