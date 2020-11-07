package com.keygenqt.mylibrary.security

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

class JWTAuthorizationFilter(private val repository: UserRepository) : OncePerRequestFilter() {

    companion object {
        private const val HEADER = "Authorization"
        private const val PREFIX = "Bearer "

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
        repository.findByToken(token)?.let {
            return Jwts.parser()
                .setSigningKey(WebSecurityConfig.SECRET_KEY.toByteArray())
                .parseClaimsJws(token.replace(PREFIX, ""))
                .body
        } ?: run {
            throw ResponseStatusException(FORBIDDEN, "Authorization failed")
        }
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
}