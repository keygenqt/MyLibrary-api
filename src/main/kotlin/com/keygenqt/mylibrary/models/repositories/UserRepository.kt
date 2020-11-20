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
import org.springframework.data.repository.query.*
import org.springframework.data.rest.core.annotation.*

interface UserRepository : PagingAndSortingRepository<User, Long> {
    @Query(value = "select u from User u where u.enabled=true and u.email=:email")
    fun findAllByEmail(@Param("email") email: String): User?

    @Query(value = "select u from User u where u.enabled=true")
    override fun findAll(pageable: Pageable): Page<User>

    @Query(value = "select u from User u where u.enabled=true and u.id=:id")
    fun findByIdActive(id: Long): User?

    @RestResource(exported = false)
    fun save(s: User): User
}