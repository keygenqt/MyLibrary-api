package com.keygenqt.mylibrary.users

import com.fasterxml.jackson.annotation.*
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "email", nullable = true)
    var email: String = "",

    @JsonIgnore
    @Column(name = "password", nullable = true)
    var password: String = "",

    @JsonIgnore
    @Column(name = "enabled", nullable = true)
    var enabled: Boolean = true,

    @JsonIgnore
    @Column(name = "role", nullable = true)
    var role: String = "USER"
)