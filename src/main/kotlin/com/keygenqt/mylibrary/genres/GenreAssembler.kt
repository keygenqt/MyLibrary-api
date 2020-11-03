package com.keygenqt.mylibrary.genres

import org.springframework.hateoas.*
import org.springframework.hateoas.server.*
import org.springframework.hateoas.server.mvc.*
import org.springframework.stereotype.*

@Component
internal class GenreAssembler : RepresentationModelAssembler<Genre, EntityModel<Genre>> {
    override fun toModel(model: Genre): EntityModel<Genre> {
        return EntityModel.of<Genre>(model,
            linkTo<GenreController> { one(model.id!!) }.withSelfRel(),
            linkTo<GenreController> { all() }.withRel("genres"))
    }
}