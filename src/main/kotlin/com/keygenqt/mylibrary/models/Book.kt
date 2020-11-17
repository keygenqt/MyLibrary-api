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

package com.keygenqt.mylibrary.models

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

    @Column(name = "user_id", nullable = false)
    var userId: Int = 0,

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

    @Column(name = "sale", nullable = true)
    var sale: Boolean = false,

    @OneToOne(cascade = [CascadeType.ALL])
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "genre_id", referencedColumnName = "id")
    var genre: Genre? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, insertable = false)
    var user: User? = null
)