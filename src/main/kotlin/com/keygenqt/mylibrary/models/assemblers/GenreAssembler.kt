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