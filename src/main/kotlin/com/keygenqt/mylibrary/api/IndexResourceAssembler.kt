package com.keygenqt.mylibrary.api

import com.keygenqt.mylibrary.api.resources.*
import com.keygenqt.mylibrary.books.*
import com.keygenqt.mylibrary.config.*
import com.keygenqt.mylibrary.models.*
import com.keygenqt.mylibrary.users.*
import org.springframework.beans.factory.annotation.*
import org.springframework.hateoas.*
import org.springframework.hateoas.server.*
import org.springframework.security.core.context.*
import org.springframework.stereotype.*
import org.springframework.web.servlet.support.*

@Service
class IndexResourceAssembler @Autowired constructor(private val relProvider: LinkRelationProvider, private val entityLinks: EntityLinks) {
    fun buildIndex(): IndexResource {
        val role = SecurityContextHolder.getContext().authentication.authorities.first().authority ?: ""
        val links: List<Link> = listOfNotNull(
            entityLinks.linkToCollectionResource(Book::class.java)
                .withRel(relProvider.getCollectionResourceRelFor(Book::class.java)),
            entityLinks.linkToCollectionResource(Genre::class.java)
                .withRel(relProvider.getCollectionResourceRelFor(Genre::class.java)),
            if (role == WebSecurityConfig.ROLE_ADMIN) {
                entityLinks.linkToCollectionResource(User::class.java)
                    .withRel(relProvider.getCollectionResourceRelFor(User::class.java))
            } else null,
            if (role == WebSecurityConfig.ROLE_ADMIN) {
                Link.of(ServletUriComponentsBuilder.fromCurrentContextPath().path("profile").build().toUriString(), "profile")
            } else null
        )
        val resource = IndexResource("1.0.0", "A sample HATEOAS API")
        resource.add(links)
        return resource
    }
}