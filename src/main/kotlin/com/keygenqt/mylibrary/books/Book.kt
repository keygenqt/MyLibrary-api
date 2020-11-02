package com.keygenqt.mylibrary.books

import com.keygenqt.mylibrary.genres.*
import com.keygenqt.mylibrary.users.*
import javax.persistence.*

// model
@Entity
@Table(name = "books")
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "user_id", nullable = true)
    var userId: Long = 0,

    @Column(name = "genre_id", nullable = true)
    var genreId: Long = 0,

    @Column(name = "title", nullable = true)
    var title: String = "",

    @Column(name = "author", nullable = true)
    var author: String = "",

    @Column(name = "description", nullable = true)
    var description: String = "",

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
    var cover: String = "",

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "id", referencedColumnName = "genre_id")
    var genre: Genre? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    var user: User? = null
)