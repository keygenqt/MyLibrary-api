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
import com.keygenqt.mylibrary.base.*
import com.keygenqt.mylibrary.extensions.*
import com.keygenqt.mylibrary.models.assemblers.*
import com.keygenqt.mylibrary.models.repositories.*
import com.keygenqt.mylibrary.security.*
import org.springframework.beans.factory.annotation.*
import org.springframework.data.repository.*
import org.springframework.http.*
import org.springframework.http.HttpStatus.*
import org.springframework.security.crypto.bcrypt.*
import org.springframework.validation.*
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
    private lateinit var updateUserMessageTokenValidator: UpdateUserMessageTokenValidator

    @Autowired
    private lateinit var passwordValidator: PasswordValidator

    @Autowired
    private lateinit var assembler: UserAssembler

    @Autowired
    private lateinit var repositoryToken: UserTokenRepository

    @PostMapping(path = ["/password"])
    fun password(@RequestBody model: PasswordBody, request: HttpServletRequest, bindingResult: BindingResult): ResponseEntity<Any> {
        repositoryToken.findByToken(request.getHeader(JWTAuthorizationFilter.HEADER))?.let { modelToken ->
            repository.findById(modelToken.userId).get().let { user ->

                passwordValidator.validate(model, bindingResult)

                return if (bindingResult.hasErrors()) {
                    bindingResult.getErrorFormat()
                } else {
                    user.password = BCryptPasswordEncoder().encode(model.password)
                    repository.save(user)
                    ResponseEntity(assembler.toModel(user), OK)
                }
            }
        }
        throw ResponseStatusException(BAD_REQUEST, "Error update password")
    }

    @GetMapping(path = ["/users/{id}"])
    fun userById(@PathVariable id: Long): ResponseEntity<Any> {
        repository.findByIdOrNull(id)?.let { model ->
            model.token = null
            return ResponseEntity(assembler.toModel(model), OK)
        }
        throw ResponseStatusException(NOT_FOUND, "Page not found")
    }

    @PutMapping(path = ["/users/{id}"])
    fun update(@PathVariable id: Long, @RequestBody model: UpdateUserBody, bindingResult: BindingResult): ResponseEntity<Any> {

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

    @PutMapping(path = ["/message-token"])
    fun messageToken(@RequestBody model: UpdateUserMessageToken, request: HttpServletRequest, bindingResult: BindingResult): ResponseEntity<Any> {

        updateUserMessageTokenValidator.validate(model, bindingResult)

        if (bindingResult.hasErrors()) {
            return bindingResult.getErrorFormat()
        } else {
            repositoryToken.findByToken(request.getHeader(JWTAuthorizationFilter.HEADER))?.let { modelToken ->
                modelToken.messageToken = model.token!!
                repositoryToken.save(modelToken)
                return getSuccessFormat("Updated message token successfully")
            }
        }

        throw ResponseStatusException(BAD_REQUEST, "Error update message token")
    }
}