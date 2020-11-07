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

package com.keygenqt.mylibrary.models.assemblers

import com.keygenqt.mylibrary.models.*
import org.springframework.beans.factory.annotation.*
import org.springframework.hateoas.*
import org.springframework.hateoas.server.*
import org.springframework.stereotype.*

@Component
internal class GenreAssembler : RepresentationModelAssembler<Genre, EntityModel<Genre>> {

    @Autowired
    private lateinit var relProvider: LinkRelationProvider

    @Autowired
    private lateinit var entityLinks: EntityLinks

    override fun toModel(model: Genre): EntityModel<Genre> {
        return EntityModel.of<Genre>(model,
            entityLinks.linkToItemResource(Genre::class.java, model.id!!).withSelfRel(),
            entityLinks.linkToItemResource(Genre::class.java, model.id!!)
                .withRel(relProvider.getItemResourceRelFor(Genre::class.java))
        )
    }
}