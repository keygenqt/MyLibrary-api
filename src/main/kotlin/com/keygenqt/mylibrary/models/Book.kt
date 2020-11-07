package com.keygenqt.mylibrary.models

import com.keygenqt.mylibrary.models.*
import org.hibernate.annotations.*
import javax.persistence.*
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "books")
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

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

    @Column(name = "image", nullable = true)
    var image: String = "",

    @OneToOne(cascade = [CascadeType.ALL])
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "genre_id", referencedColumnName = "id")
    var genre: Genre? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    var user: User? = null
)