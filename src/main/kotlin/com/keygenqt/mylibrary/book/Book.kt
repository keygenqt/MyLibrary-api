package com.keygenqt.mylibrary.book

import javax.persistence.*

// model
@Entity
@Table(name = "books")
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name", nullable = true)
    var name: String = "",

    @Column(name = "author", nullable = true)
    var author: String = "",

    @Column(name = "publisher", nullable = true)
    var publisher: String = "",

    @Column(name = "year", nullable = true)
    var year: Int = 0,

    @Column(name = "isbn", nullable = true)
    var ISBN: String = "",

    @Column(name = "number_of_pages", nullable = true)
    var numberOfPages: Int = 0,

    @Column(name = "cover_type", nullable = true)
    var coverType: String = "soft",

    @Column(name = "cover", nullable = true)
    var cover: String = ""
)