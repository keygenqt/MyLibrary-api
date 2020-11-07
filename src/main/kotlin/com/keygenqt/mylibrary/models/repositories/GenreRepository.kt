package com.keygenqt.mylibrary.models.repositories

import com.keygenqt.mylibrary.models.*
import org.springframework.data.repository.*

internal interface GenreRepository : PagingAndSortingRepository<Genre, Long> {
    fun findAllByTitle(title: String): List<Genre>
    fun findAllByDescription(description: String): List<Genre>
    fun findAllByTitleAndDescription(title: String, description: String): List<Genre>
}