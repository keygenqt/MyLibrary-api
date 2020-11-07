package com.keygenqt.mylibrary.models.repositories

import com.keygenqt.mylibrary.models.*
import org.springframework.data.domain.*
import org.springframework.data.jpa.repository.*
import org.springframework.data.repository.*
import org.springframework.data.repository.query.*

interface UserRepository : PagingAndSortingRepository<User, Long> {
    @Query(value = "select u from User u where u.enabled=true and u.email=:email")
    fun findAllByEmail(@Param("email") email: String): User?

    @Query(value = "select u from User u where u.enabled=true and u.token=:token")
    fun findByToken(@Param("token") token: String): User?

    @Query(value = "select u from User u where u.enabled=true")
    override fun findAll(pageable: Pageable): Page<User>
}