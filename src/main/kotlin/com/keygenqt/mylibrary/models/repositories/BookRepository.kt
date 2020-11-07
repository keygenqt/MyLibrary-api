package com.keygenqt.mylibrary.models.repositories

import com.keygenqt.mylibrary.models.*
import org.springframework.data.repository.*

internal interface BookRepository : PagingAndSortingRepository<Book, Long>