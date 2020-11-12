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
import com.keygenqt.mylibrary.models.*
import com.keygenqt.mylibrary.models.assemblers.*
import com.keygenqt.mylibrary.models.repositories.*
import com.keygenqt.mylibrary.security.*
import org.springframework.beans.factory.annotation.*
import org.springframework.http.*
import org.springframework.http.HttpStatus.*
import org.springframework.validation.*
import org.springframework.validation.annotation.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.*
import java.sql.*

@RestController
@Validated
class AuthController {

    @Autowired
    private lateinit var repository: UserRepository

    @Autowired
    private lateinit var loginValidator: LoginValidator

    @Autowired
    private lateinit var repositoryToken: UserTokenRepository

    @Autowired
    private lateinit var assembler: UserAssembler

    @PostMapping(
        path = ["/login"],
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun login(model: Login, bindingResult: BindingResult): ResponseEntity<Any> {

        loginValidator.validate(model, bindingResult)

        if (bindingResult.hasErrors()) {
            return ResponseEntity(hashMapOf<String, Any>("errors" to bindingResult.fieldErrors).apply {
                put("status", UNPROCESSABLE_ENTITY.value())
                put("error", "Validate")
                put("message", "Validation failed")
                put("path", "/login")
                put("timestamp", Timestamp(System.currentTimeMillis()))
            }, UNPROCESSABLE_ENTITY)
        } else {
            repository.findAllByEmail(model.email ?: "")?.let { user ->
                val tokenModel = user.tokens.firstOrNull { it.uid == model.uid }?.let {
                    it.token = JWTAuthorizationFilter.getJWTToken(user.email, user.role)
                    it
                } ?: run {
                    UserToken(
                        uid = model.uid!!,
                        userId = user.id!!,
                        token = JWTAuthorizationFilter.getJWTToken(user.email, user.role)
                    )
                }
                tokenModel.token = JWTAuthorizationFilter.getJWTToken(user.email, user.role)
                repositoryToken.save(tokenModel)
                user.token = tokenModel.token
                return ResponseEntity(assembler.toModel(user), OK)
            }
        }
        throw ResponseStatusException(FORBIDDEN, "Authorization failed")
    }
}