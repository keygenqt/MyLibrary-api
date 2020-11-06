package com.keygenqt.mylibrary.extension

import com.keygenqt.mylibrary.config.*
import io.jsonwebtoken.*
import org.springframework.security.core.authority.*
import java.util.*

fun String.getJWTToken(role: String): String {
    return "Bearer ${Jwts
        .builder()
        .setId("JWT")
        .setSubject(this)
        .claim("authorities", listOf(SimpleGrantedAuthority(role)))
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + 2592000000 /* month */))
        .signWith(SignatureAlgorithm.HS512, WebSecurityConfig.SECRET_KEY.toByteArray())
        .compact()}"
}