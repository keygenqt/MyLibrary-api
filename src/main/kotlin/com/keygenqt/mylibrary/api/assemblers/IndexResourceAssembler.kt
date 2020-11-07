package com.keygenqt.mylibrary.api.assemblers

import com.keygenqt.mylibrary.api.resources.*
import com.keygenqt.mylibrary.models.*
import com.keygenqt.mylibrary.security.*
import org.springframework.beans.factory.annotation.*
import org.springframework.hateoas.*
import org.springframework.hateoas.server.*
import org.springframework.security.core.context.*
import org.springframework.stereotype.*
import org.springframework.web.servlet.support.*

@Service
class IndexResourceAssembler {

    @Autowired
    private lateinit var relProvider: LinkRelationProvider

    @Autowired
    private lateinit var entityLinks: EntityLinks

    fun buildIndex(): IndexResource {
        val role = SecurityContextHolder.getContext().authentication.authorities.first().authority ?: WebSecurityConfig.ROLE_USER
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
        val resource = IndexResource("1.0.0", "HATEOAS API for app My Library")
        resource.add(links)
        return resource
    }
}