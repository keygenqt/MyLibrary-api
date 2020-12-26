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

import com.keygenqt.mylibrary.api.bodies.JoinBody
import com.keygenqt.mylibrary.api.validators.JoinValidator
import com.keygenqt.mylibrary.api.bodies.LoginBody
import com.keygenqt.mylibrary.api.validators.LoginValidator
import com.keygenqt.mylibrary.base.BaseFormatResponse
import com.keygenqt.mylibrary.base.JWTAuthorizationFilter
import com.keygenqt.mylibrary.models.User
import com.keygenqt.mylibrary.models.UserToken
import com.keygenqt.mylibrary.models.assemblers.UserAssembler
import com.keygenqt.mylibrary.models.repositories.UserRepository
import com.keygenqt.mylibrary.models.repositories.UserTokenRepository
import com.keygenqt.mylibrary.config.WebSecurityConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import javax.servlet.http.HttpServletRequest

@RestController
@Validated
class AuthController {

    @Autowired
    private lateinit var repository: UserRepository

    @Autowired
    private lateinit var loginValidator: LoginValidator

    @Autowired
    private lateinit var regValidator: JoinValidator

    @Autowired
    private lateinit var repositoryToken: UserTokenRepository

    @Autowired
    private lateinit var assembler: UserAssembler

    @PostMapping(path = ["/login"])
    fun login(@RequestBody model: LoginBody, bindingResult: BindingResult, request: HttpServletRequest): ResponseEntity<Any> {

        loginValidator.validate(model, bindingResult)

        if (bindingResult.hasErrors()) {
            return BaseFormatResponse.getErrorFormat(bindingResult)
        } else {
            repository.findAllByEmail(model.email ?: "")?.let { user ->
                val tokenModel = user.tokens.firstOrNull { it.uid == model.uid }?.let {
                    it.token = JWTAuthorizationFilter.getJWTToken(user.email, user.role)
                    it
                } ?: run {
                    UserToken(
                            model.uid,
                            user.id,
                            JWTAuthorizationFilter.getJWTToken(user.email, user.role),
                            request.getHeader(JWTAuthorizationFilter.ACCEPT_LANGUAGE)
                    )
                }
                tokenModel.token = JWTAuthorizationFilter.getJWTToken(user.email, user.role)
                repositoryToken.save(tokenModel)
                user.token = tokenModel.token
                return ResponseEntity(assembler.toModel(user), OK)
            }
        }
        throw ResponseStatusException(UNAUTHORIZED, "Authorization failed")
    }

    @PostMapping(path = ["/join"])
    fun join(@RequestBody model: JoinBody, bindingResult: BindingResult, request: HttpServletRequest): ResponseEntity<Any> {

        regValidator.validate(model, bindingResult)

        if (bindingResult.hasErrors()) {
            return BaseFormatResponse.getErrorFormat(bindingResult)
        } else {
            repository.save(
                    User(
                            model.email,
                            model.nickname,
                            BCryptPasswordEncoder().encode(model.password),
                            WebSecurityConfig.ROLE_USER,
                            model.avatar
                    )
            )
            repository.findAllByEmail(model.email ?: "")?.let { user ->
                val tokenModel = UserToken(
                        model.uid,
                        user.id,
                        JWTAuthorizationFilter.getJWTToken(user.email, user.role),
                        request.getHeader(JWTAuthorizationFilter.ACCEPT_LANGUAGE)
                )
                tokenModel.token = JWTAuthorizationFilter.getJWTToken(user.email, user.role)
                repositoryToken.save(tokenModel)
                user.token = tokenModel.token
                return ResponseEntity(assembler.toModel(user), OK)
            }
        }
        throw ResponseStatusException(UNAUTHORIZED, "Join failed")
    }
}