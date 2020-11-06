package com.keygenqt.mylibrary.common

import com.keygenqt.mylibrary.books.*
import com.keygenqt.mylibrary.config.WebSecurityConfig.Companion.ROLE_ADMIN
import com.keygenqt.mylibrary.genres.*
import com.keygenqt.mylibrary.users.*
import net.minidev.json.*
import org.springframework.hateoas.*
import org.springframework.hateoas.server.mvc.*
import org.springframework.http.*
import org.springframework.http.HttpStatus.*
import org.springframework.security.core.context.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.*
import java.net.*
import java.nio.charset.*
import javax.servlet.http.*

@RestController
internal class MainController {

    @GetMapping(path = ["/"]) fun main(
        request: HttpServletRequest,
        @RequestHeader host: String
    ): ResponseEntity<JSONObject> {

        SecurityContextHolder.getContext().authentication.authorities.first().authority?.let { role ->
            val obj = JSONObject(hashMapOf("_links" to JSONObject()))

            // models
            val linksModels = mutableListOf(
                linkTo<BookController> { all() }.withRel("books")
                    .getUrlDefaultParamsPageableController<BookController>("all"),
                linkTo<GenreController> { all() }.withRel("genres")
                    .getUrlDefaultParamsPageableController<GenreController>("all")
            )
            if (role == ROLE_ADMIN) {
                linksModels.addAll(
                    listOf(
                        linkTo<UserController> { all() }.withRel("users")
                            .getUrlDefaultParamsPageableController<UserController>("all")
                    )
                )
            }
            linksModels.forEach { link ->
                val links = obj["_links"] as JSONObject
                links[link.rel.value()] = JSONObject(hashMapOf("href"
                    to URLDecoder.decode(link.href, StandardCharsets.UTF_8.name())))
            }

            // other
            val linksOther = mutableListOf(
                linkTo<UserController> { login("{login}", "{password}") }.withRel("login")
            )
            if (role == ROLE_ADMIN) {
                linksOther.addAll(
                    listOf(
                        Link.of("${request.scheme}/$host/profile", "profile")
                    )
                )
            }
            linksOther.forEach { link ->
                val links = obj["_links"] as JSONObject
                links[link.rel.value()] = JSONObject(hashMapOf("href"
                    to URLDecoder.decode(link.href, StandardCharsets.UTF_8.name())))
            }

            return ResponseEntity(obj, OK)
        } ?: run {
            throw ResponseStatusException(FORBIDDEN, "Authorization failed")
        }
    }
}