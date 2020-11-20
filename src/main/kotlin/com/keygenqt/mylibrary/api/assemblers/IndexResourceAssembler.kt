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
            entityLinks.linkToCollectionResource(Book::class.java).withRel(relProvider.getCollectionResourceRelFor(Book::class.java)),
            entityLinks.linkToCollectionResource(Genre::class.java).withRel(relProvider.getCollectionResourceRelFor(Genre::class.java)),
            entityLinks.linkToCollectionResource(User::class.java).withRel(relProvider.getCollectionResourceRelFor(User::class.java)),
            if (role == WebSecurityConfig.ROLE_ADMIN) {
                Link.of(ServletUriComponentsBuilder.fromCurrentContextPath().path("profile").build().toUriString(), "profile")
            } else null
        )
        val resource = IndexResource("1.0.0", "HATEOAS API for app My Library")
        resource.add(links)
        return resource
    }
}