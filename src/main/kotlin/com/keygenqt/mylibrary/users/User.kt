package com.keygenqt.mylibrary.users

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.annotation.JsonInclude.*
import javax.persistence.*

@Entity
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "email", nullable = true)
    var email: String = "",

    @Column(name = "login", nullable = true)
    var login: String = "",

    @Column(name = "image", nullable = true)
    var image: String = "",

    @JsonIgnore
    @Column(name = "password", nullable = true)
    var password: String = "",

    @JsonIgnore
    @Column(name = "enabled", nullable = true)
    var enabled: Boolean = true,

    @JsonIgnore
    @Column(name = "role", nullable = true)
    var role: String = "USER",

    @Transient
    @JsonInclude(Include.NON_NULL)
    var token: String? = null
)