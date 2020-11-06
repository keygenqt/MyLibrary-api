package com.keygenqt.mylibrary.users

import org.springframework.data.repository.*

//@RepositoryRestResource(exported = false)
interface UserRepository : PagingAndSortingRepository<User, Long> {
    fun findAllByEmail(email: String): User?
}