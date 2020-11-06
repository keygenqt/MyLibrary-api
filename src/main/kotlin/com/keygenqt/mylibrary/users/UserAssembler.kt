package com.keygenqt.mylibrary.users

import org.springframework.hateoas.*
import org.springframework.hateoas.server.*
import org.springframework.hateoas.server.mvc.*
import org.springframework.stereotype.*

@Component
internal class UserAssembler : RepresentationModelAssembler<User, EntityModel<User>> {
    override fun toModel(model: User): EntityModel<User> {
        return EntityModel.of<User>(model,
            linkTo<UserController> { one(model.id!!) }.withSelfRel(),
            linkTo<UserController> { all() }.withRel("genres"))
    }
}