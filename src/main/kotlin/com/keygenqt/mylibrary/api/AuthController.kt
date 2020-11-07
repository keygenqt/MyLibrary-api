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
    private lateinit var repositoryToken: UserTokenRepository

    @Autowired
    private lateinit var assembler: UserAssembler

    @PostMapping(path = ["/login"])
    fun login(
        @RequestHeader("uid") uid: String,
        @RequestParam(required = true) email: String,
        @RequestParam(required = true) password: String
    ): EntityModel<User> {
        repository.findAllByEmail(email)?.let { model ->
            if (BCryptPasswordEncoder().matches(password, model.password)) {
                val tokenModel = model.tokens.firstOrNull { it.uid == uid }?.let {
                    it.token = JWTAuthorizationFilter.getJWTToken(model.email, model.role)
                    it
                } ?: run {
                    UserToken(
                        uid = uid,
                        userId = model.id!!,
                        token = JWTAuthorizationFilter.getJWTToken(model.email, model.role)
                    )
                }
                tokenModel.token = JWTAuthorizationFilter.getJWTToken(model.email, model.role)
                repositoryToken.save(tokenModel)
                model.token = tokenModel.token
                return assembler.toModel(model)
            }
        }
        throw ResponseStatusException(FORBIDDEN, "Authorization failed")
    }
}