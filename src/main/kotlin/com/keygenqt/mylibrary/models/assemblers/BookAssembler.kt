package com.keygenqt.mylibrary.models.assemblers

import com.keygenqt.mylibrary.models.*
import org.springframework.beans.factory.annotation.*
import org.springframework.hateoas.*
import org.springframework.hateoas.server.*
import org.springframework.stereotype.*

@Component
internal class BookAssembler : RepresentationModelAssembler<Book, EntityModel<Book>> {

    @Autowired
    private lateinit var relProvider: LinkRelationProvider

    @Autowired
    private lateinit var entityLinks: EntityLinks

    override fun toModel(model: Book): EntityModel<Book> {
        return EntityModel.of<Book>(model,
            entityLinks.linkToItemResource(Book::class.java, model.id!!).withSelfRel(),
            entityLinks.linkToItemResource(Book::class.java, model.id!!)
                .withRel(relProvider.getItemResourceRelFor(Book::class.java))
        )
    }
}