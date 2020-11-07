package com.keygenqt.mylibrary.api.resources

import com.keygenqt.mylibrary.models.*
import org.springframework.hateoas.*

class IndexResource(val version: String, val description: String) : RepresentationModel<EntityModel<IndexResource>>()
