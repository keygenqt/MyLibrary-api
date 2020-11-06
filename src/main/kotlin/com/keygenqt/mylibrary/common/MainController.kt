package com.keygenqt.mylibrary.common

import com.keygenqt.mylibrary.books.*
import com.keygenqt.mylibrary.config.WebSecurityConfig.Companion.ROLE_ADMIN
import com.keygenqt.mylibrary.genres.*
import com.keygenqt.mylibrary.users.*
import net.minidev.json.*
import org.springframework.beans.factory.annotation.*
import org.springframework.hateoas.*
import org.springframework.hateoas.server.*
import org.springframework.http.*
import org.springframework.http.HttpStatus.*
import org.springframework.security.core.context.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.*
import javax.servlet.http.*

@RestController
internal class MainController {

    @Autowired
    lateinit var entityLinks: EntityLinks

    @GetMapping(path = ["/"]) fun main(
        request: HttpServletRequest,
        @RequestHeader host: String
    ): ResponseEntity<JSONObject> {
        SecurityContextHolder.getContext().authentication.authorities.first().authority?.let { role ->
            val obj = JSONObject(hashMapOf("_links" to JSONObject()))
            listOfNotNull(
                if (role == ROLE_ADMIN) entityLinks.linkToCollectionResource(User::class.java) else null,
                if (role == ROLE_ADMIN) Link.of("${request.scheme}/$host/profile", "profile") else null,
                entityLinks.linkToCollectionResource(Book::class.java),
                entityLinks.linkToCollectionResource(Genre::class.java)
            ).forEach { link ->
                val links = obj["_links"] as JSONObject
                links[link.rel.value()] = JSONObject(hashMapOf("href" to link.href))
            }
            return ResponseEntity(obj, OK)
        } ?: run {
            throw ResponseStatusException(FORBIDDEN, "Authorization failed")
        }
    }
}