package com.keygenqt.mylibrary.api.resources

import org.springframework.hateoas.*

class IndexResource(
    val version: String,
    val description: String
) : RepresentationModel<EntityModel<IndexResource>>()
