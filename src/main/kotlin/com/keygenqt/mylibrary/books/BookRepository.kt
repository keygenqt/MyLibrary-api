package com.keygenqt.mylibrary.books

import org.springframework.data.domain.*
import org.springframework.data.jpa.repository.*

internal interface BookRepository : JpaRepository<Book, Long> {
    @Query("select model from Book model")
    fun findAllPage(pageable: Pageable): Page<Book>
}