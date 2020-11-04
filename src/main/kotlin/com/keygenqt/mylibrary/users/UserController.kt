package com.keygenqt.mylibrary.users

import com.keygenqt.mylibrary.extension.*
import org.springframework.beans.factory.annotation.*
import org.springframework.http.HttpStatus.*
import org.springframework.security.crypto.bcrypt.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.*

@RestController
class UserController {

    @Autowired
    private lateinit var repository: UserRepository

    @PostMapping("/login")
    fun login(@RequestParam("email") email: String, @RequestParam("password") password: String): User {
        repository.findAllByEmail(email)?.let {
            if (BCryptPasswordEncoder().matches(password, it.password)) {
                it.token = it.email.getJWTToken()
                return it
            }
        }
        throw ResponseStatusException(FORBIDDEN, "Authorization failed")
    }

}