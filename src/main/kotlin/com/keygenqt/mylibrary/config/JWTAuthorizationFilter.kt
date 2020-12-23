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

package com.keygenqt.mylibrary.config

import com.keygenqt.mylibrary.models.*
import com.keygenqt.mylibrary.models.repositories.*
import io.jsonwebtoken.*
import org.springframework.http.HttpStatus.*
import org.springframework.security.authentication.*
import org.springframework.security.core.authority.*
import org.springframework.security.core.context.*
import org.springframework.web.filter.*
import org.springframework.web.server.*
import java.util.*
import javax.servlet.*
import javax.servlet.http.*

class JWTAuthorizationFilter(
    private val repositoryUser: UserRepository,
    private val repositoryUserToken: UserTokenRepository
) : OncePerRequestFilter() {

    companion object {
        const val ACCEPT_LANGUAGE = "Accept-Language"
        const val HEADER = "Authorization"
        const val PREFIX = "Bearer "

        fun getJWTToken(login: String, role: String): String {
            return "Bearer ${
                Jwts
                    .builder()
                    .setId("JWT")
                    .setSubject(login)
                    .claim("authorities", listOf(SimpleGrantedAuthority(role)))
                    .setIssuedAt(Date(System.currentTimeMillis()))
                    .setExpiration(Date(System.currentTimeMillis() + 2592000000 /* month */))
                    .signWith(SignatureAlgorithm.HS512, WebSecurityConfig.SECRET_KEY.toByteArray())
                    .compact()
            }"
        }
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        try {
            if (checkJWTToken(request)) {
                val claims = validateToken(request)
                if (claims["authorities"] != null) {
                    setUpSpringAuthentication(claims)
                    chain.doFilter(request, response)
                    return
                }
            }
        } catch (ex: Exception) {
        }
        SecurityContextHolder.clearContext()
        chain.doFilter(request, response)
    }

    private fun validateToken(request: HttpServletRequest): Claims {
        val token = request.getHeader(HEADER)
        val language = request.getHeader(ACCEPT_LANGUAGE)
        repositoryUserToken.findByToken(token)?.let { userToken ->
            repositoryUser.findByIdActive(userToken.userId)?.let { user ->
                if (user.enabled) {
                    update(userToken, language)
                    return Jwts.parser()
                        .setSigningKey(WebSecurityConfig.SECRET_KEY.toByteArray())
                        .parseClaimsJws(token.replace(PREFIX, ""))
                        .body
                } else {
                    throw ResponseStatusException(UNAUTHORIZED, "Authorization failed. User disabled.")
                }
            }
        }
        throw ResponseStatusException(UNAUTHORIZED, "Authorization failed")
    }

    private fun setUpSpringAuthentication(claims: Claims) {
        claims["authorities"]?.let {
            if (it is ArrayList<*>) {
                it.first()?.let { item ->
                    if (item is LinkedHashMap<*, *>) {
                        item["authority"]?.let { authority ->
                            SecurityContextHolder.getContext().authentication =
                                UsernamePasswordAuthenticationToken(claims.subject, null, listOf(SimpleGrantedAuthority(authority.toString())))
                        }
                    }
                }
            }
        }
    }

    private fun checkJWTToken(request: HttpServletRequest): Boolean {
        request.getHeader(HEADER)?.let {
            return it.startsWith(PREFIX)
        }
        return false
    }

    private fun update(userToken: UserToken, language: String) {
        var isUpdate = false
        if (language != userToken.language) {
            userToken.language = language
            isUpdate = true
        }
        if (userToken.updatedAt.time < (Date().time - 86400000)) {
            userToken.updatedAt = Date()
            isUpdate = true
        }
        if (isUpdate) {
            repositoryUserToken.save(userToken)
        }
    }
}