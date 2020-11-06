package com.keygenqt.mylibrary.common

import com.keygenqt.mylibrary.config.WebSecurityConfig.Companion.SECRET_KEY
import io.jsonwebtoken.*
import org.springframework.security.authentication.*
import org.springframework.security.core.authority.*
import org.springframework.security.core.context.*
import org.springframework.web.filter.*
import javax.servlet.*
import javax.servlet.http.*

class JWTAuthorizationFilter : OncePerRequestFilter() {

    companion object {
        private const val HEADER = "Authorization"
        private const val PREFIX = "Bearer "
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
        val token = request.getHeader(HEADER).replace(PREFIX, "")
        return Jwts.parser()
            .setSigningKey(SECRET_KEY.toByteArray())
            .parseClaimsJws(token)
            .body
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