///*
// * Copyright 2020 Vitaliy Zarubin
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.keygenqt.mylibrary.models
//
//import com.fasterxml.jackson.annotation.*
//import org.hibernate.annotations.*
//import java.util.*
//import javax.persistence.*
//import javax.persistence.CascadeType
//import javax.persistence.Entity
//import javax.persistence.Table
//
//@Entity
//@Table(name = "books")
//data class Book(
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    var id: Long? = null,
//
//    @Column(name = "genre_id", nullable = false)
//    var genreId: Long = 0,
//
//    @Column(name = "user_id", nullable = false)
//    var userId: Long = 0,
//
//    @Column(name = "image", nullable = true)
//    var image: String? = null,
//
//    @Column(name = "title", nullable = true)
//    var title: String? = null,
//
//    @Column(name = "author", nullable = true)
//    var author: String? = null,
//
//    @Column(name = "publisher", nullable = true)
//    var publisher: String? = null,
//
//    @Column(name = "isbn", nullable = true)
//    var ISBN: String? = null,
//
//    @Column(name = "year", nullable = true)
//    var year: Int? = null,
//
//    @Column(name = "number_of_pages", nullable = true)
//    var numberOfPages: Int? = null,
//
//    @Column(name = "description", nullable = true)
//    var description: String? = null,
//
//    @Column(name = "cover_type", nullable = false)
//    var coverType: String = "soft",
//
//    @Column(name = "sale", nullable = false)
//    var sale: Boolean = false,
//
//    @Column(name = "enabled", nullable = false)
//    var enabled: Boolean = true,
//
//    @JsonIgnore
//    @CreationTimestamp
//    @Column(name = "created_at")
//    @Temporal(TemporalType.TIMESTAMP)
//    var createdAt: Date = Date(),
//
//    @JsonIgnore
//    @UpdateTimestamp
//    @Column(name = "updated_at")
//    @Temporal(TemporalType.TIMESTAMP)
//    var updatedAt: Date = Date(),
//
//    @OneToOne(cascade = [CascadeType.ALL])
//    @NotFound(action = NotFoundAction.IGNORE)
//    @JoinColumn(name = "genre_id", referencedColumnName = "id", updatable = false, insertable = false)
//    var genre: Genre? = null,
//
//    @OneToOne(cascade = [CascadeType.ALL])
//    @NotFound(action = NotFoundAction.IGNORE)
//    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, insertable = false)
//    var user: User? = null
//) {
//    companion object {
//        const val COVER_SOFT = "Soft"
//        const val COVER_SOLID = "Solid"
//        const val COVER_OTHER = "Other"
//    }
//}