package com.keygenqt.mylibrary.models.assemblers

import com.keygenqt.mylibrary.models.*
import org.springframework.beans.factory.annotation.*
import org.springframework.hateoas.*
import org.springframework.hateoas.server.*
import org.springframework.stereotype.*

@Component
internal class UserAssembler : RepresentationModelAssembler<User, EntityModel<User>> {

    @Autowired
    private lateinit var relProvider: LinkRelationProvider

    @Autowired
    private lateinit var entityLinks: EntityLinks

    override fun toModel(model: User): EntityModel<User> {
        return EntityModel.of<User>(model,
            entityLinks.linkToItemResource(User::class.java, model.id!!).withSelfRel(),
            entityLinks.linkToItemResource(User::class.java, model.id!!)
                .withRel(relProvider.getItemResourceRelFor(User::class.java))
        )
    }
}