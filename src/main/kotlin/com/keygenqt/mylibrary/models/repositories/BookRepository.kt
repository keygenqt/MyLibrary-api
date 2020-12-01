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

package com.keygenqt.mylibrary.models.repositories

import com.keygenqt.mylibrary.models.*
import org.springframework.data.domain.*
import org.springframework.data.jpa.repository.*
import org.springframework.data.repository.*
import java.util.*

internal interface BookRepository : PagingAndSortingRepository<Book, Long> {

    @Query(value = "select m from Book m where m.enabled=true and m.id=:id")
    fun findById(id: Long?): Optional<Book?>?

    @Query(value = "select m from Book m where m.enabled=true order by m.id desc")
    override fun findAll(pageable: Pageable): Page<Book>

    @Query(value = "select m from Book m where m.enabled=true and m.id=:id order by m.id desc")
    fun findAllByUserId(id: Int, pageable: Pageable): Page<Book>

    @Query(value = "select m from Book m where m.enabled=true and m.sale=:sale order by m.id desc")
    fun findAllBySale(sale: Boolean, pageable: Pageable): Page<Book>
}