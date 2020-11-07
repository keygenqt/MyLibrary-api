package com.keygenqt.mylibrary.models

/*
 * Copyright 2020 Vitaliy Zarubin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.annotation.JsonInclude.*
import org.hibernate.annotations.*
import javax.persistence.*
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Table

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
    var token: String = "",

    @JsonIgnore
    @OneToMany(cascade = [CascadeType.ALL])
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    var tokens: List<UserToken> = listOf()
)