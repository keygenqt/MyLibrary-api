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
import com.keygenqt.mylibrary.config.*
import org.springframework.beans.factory.annotation.*
import org.springframework.hateoas.*
import org.springframework.hateoas.server.*
import org.springframework.security.core.context.*
import org.springframework.stereotype.*
import org.springframework.web.servlet.support.*

@Service
class IndexResourceAssembler {

    companion object {
        const val API_KEY_MESSAGE_TOKEN = "message-token"
        const val API_KEY_UPLOAD_IMAGE = "upload-image"
        const val API_KEY_LOGIN = "login"
        const val API_KEY_JOIN = "join"
        const val API_KEY_PASSWORD = "password"
    }

    @Autowired
    private lateinit var relProvider: LinkRelationProvider

    @Autowired
    private lateinit var entityLinks: EntityLinks

    fun buildIndex(): IndexResource {
        val role = SecurityContextHolder.getContext().authentication.authorities.first().authority ?: WebSecurityConfig.ROLE_ANONYMOUS

        val links: MutableList<Link> = mutableListOf(
            Link.of(ServletUriComponentsBuilder.fromCurrentContextPath().path("/login").build().toUriString(), API_KEY_LOGIN),
            Link.of(ServletUriComponentsBuilder.fromCurrentContextPath().path("/join").build().toUriString(), API_KEY_JOIN),
            Link.of(ServletUriComponentsBuilder.fromCurrentContextPath().path("/password").build().toUriString(), API_KEY_PASSWORD)
        )

        if (role == WebSecurityConfig.ROLE_USER || role == WebSecurityConfig.ROLE_ADMIN) {
            links.apply {
                add(Link.of(ServletUriComponentsBuilder.fromCurrentContextPath().path("/message-token").build().toUriString(), API_KEY_MESSAGE_TOKEN))
                add(Link.of(ServletUriComponentsBuilder.fromCurrentContextPath().path("/upload-image").build().toUriString(), API_KEY_UPLOAD_IMAGE))
                add(Link.of(ServletUriComponentsBuilder.fromCurrentContextPath().path("/genres/search/findAll{?language,page,size,sort}").build().toUriString())
                    .withRel(relProvider.getCollectionResourceRelFor(Genre::class.java)))
                add(entityLinks.linkToCollectionResource(Book::class.java).withRel(relProvider.getCollectionResourceRelFor(Book::class.java)))
                add(entityLinks.linkToCollectionResource(User::class.java).withRel(relProvider.getCollectionResourceRelFor(User::class.java)))
            }
        }
        if (role == WebSecurityConfig.ROLE_ADMIN) {
            links.apply {
                add(Link.of(ServletUriComponentsBuilder.fromCurrentContextPath().path("/profile").build().toUriString(), "profile"))
            }
        }

        val resource = IndexResource("1.0.0", "HATEOAS API for app MyLibrary", role.replace("ROLE_", ""))
        resource.add(links)
        return resource
    }
}