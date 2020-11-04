package com.keygenqt.mylibrary.extension

import com.keygenqt.mylibrary.common.*
import io.jsonwebtoken.*
import org.springframework.security.core.authority.*
import java.util.*

fun String.getJWTToken(): String {
    return "Bearer ${Jwts
        .builder()
        .setId("JWT")
        .setSubject(this)
        .claim("authorities", AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN,ROLE_USER").map { it.authority })
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + 2592000000 /* month */))
        .signWith(SignatureAlgorithm.HS512, WebSecurityConfig.SECRET_KEY.toByteArray())
        .compact()}"
}