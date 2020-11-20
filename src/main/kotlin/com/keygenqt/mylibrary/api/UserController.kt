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

package com.keygenqt.mylibrary.api

import com.keygenqt.mylibrary.api.validators.*
import com.keygenqt.mylibrary.extensions.*
import com.keygenqt.mylibrary.models.*
import com.keygenqt.mylibrary.models.assemblers.*
import com.keygenqt.mylibrary.models.repositories.*
import com.keygenqt.mylibrary.security.*
import com.keygenqt.mylibrary.security.JWTAuthorizationFilter.*
import com.keygenqt.mylibrary.security.WebSecurityConfig.Companion.ROLE_USER
import org.springframework.beans.factory.annotation.*
import org.springframework.data.repository.*
import org.springframework.http.*
import org.springframework.http.HttpStatus.*
import org.springframework.security.crypto.bcrypt.*
import org.springframework.validation.*
import org.springframework.validation.annotation.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.*
import javax.servlet.http.*

@RestController
class UserController {

    @Autowired
    private lateinit var repository: UserRepository

    @Autowired
    private lateinit var updateUserValidator: UpdateUserValidator

    @Autowired
    private lateinit var assembler: UserAssembler

    @GetMapping(path = ["/users/{id}"])
    fun userById(@PathVariable id: Long): ResponseEntity<Any> {
        repository.findByIdOrNull(id)?.let { model ->
            model.token = null
            return ResponseEntity(assembler.toModel(model), OK)
        }
        throw ResponseStatusException(NOT_FOUND, "Page not found")
    }

    @PutMapping(path = ["/users/{id}"])
    fun update(@PathVariable id: Long, @RequestBody model: UpdateUser, bindingResult: BindingResult): ResponseEntity<Any> {

        updateUserValidator.validate(model, bindingResult)

        if (bindingResult.hasErrors()) {
            return bindingResult.getErrorFormat()
        } else {
            repository.findByIdOrNull(id)?.let { user ->
                user.avatar = model.avatar!!
                user.nickname = model.nickname!!
                user.website = model.website
                user.location = model.location
                user.bio = model.bio
                repository.save(user)
                return ResponseEntity(assembler.toModel(user), OK)
            }
        }
        throw ResponseStatusException(BAD_REQUEST, "Error update user")
    }
}