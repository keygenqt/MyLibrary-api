package com.keygenqt.mylibrary.users

import org.springframework.data.repository.*
import org.springframework.data.rest.core.annotation.*

@RepositoryRestResource(exported = false)
interface UserRepository : CrudRepository<User, Long> {
    fun findAllByEmail(email: String): User?
}