package com.keygenqt.mylibrary.models

import javax.persistence.*

@Entity
@Table(name = "genres")
data class Genre(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "title", nullable = true)
    var title: String = "",

    @Column(name = "description", nullable = true)
    var description: String = ""
)