package com.keygenqt.mylibrary.api

import com.keygenqt.mylibrary.models.*
import com.keygenqt.mylibrary.models.assemblers.*
import com.keygenqt.mylibrary.models.repositories.*
import com.keygenqt.mylibrary.security.*
import org.springframework.beans.factory.annotation.*
import org.springframework.hateoas.*
import org.springframework.http.HttpStatus.*
import org.springframework.security.crypto.bcrypt.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.*

@RestController
class AuthController {

    @Autowired
    private lateinit var repository: UserRepository

    @Autowired
    private lateinit var assembler: UserAssembler

    @PostMapping(path = ["/login"])
    fun login(
        @RequestParam(required = true) email: String,
        @RequestParam(required = true) password: String
    ): EntityModel<User> {
        repository.findAllByEmail(email)?.let {
            if (BCryptPasswordEncoder().matches(password, it.password)) {
                it.token = JWTAuthorizationFilter.getJWTToken(it.email, it.role)
                repository.save(it)
                return assembler.toModel(it)
            }
        }
        throw ResponseStatusException(FORBIDDEN, "Authorization failed")
    }
}