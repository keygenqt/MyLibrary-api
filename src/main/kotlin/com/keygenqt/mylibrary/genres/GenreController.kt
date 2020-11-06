package com.keygenqt.mylibrary.genres

import org.springframework.beans.factory.annotation.*
import org.springframework.data.domain.*
import org.springframework.data.web.*
import org.springframework.data.web.SortDefault.*
import org.springframework.hateoas.*
import org.springframework.http.*
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.*

@RestController
internal class GenreController {

    @Autowired
    private lateinit var repository: GenreRepository

    @Autowired
    private lateinit var assembler: GenreAssembler

    @Autowired
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    private lateinit var pagedAssembler: PagedResourcesAssembler<Genre>

    @GetMapping(path = ["/genres"]) fun all(
        @PageableDefault(page = 0, size = 20)
        @SortDefaults(SortDefault(sort = ["title"], direction = Sort.Direction.DESC), SortDefault(sort = ["id"], direction = Sort.Direction.ASC))
        pageable: Pageable = Pageable.unpaged()
    ): ResponseEntity<PagedModel<EntityModel<Genre>>> {
        val collModel = pagedAssembler
            .toModel<EntityModel<Genre>>(repository.findAll(pageable), assembler)
        return ResponseEntity(collModel, OK)
    }

    @GetMapping("/genres/{id}") fun one(@PathVariable id: Long): EntityModel<Genre> {
        return assembler.toModel(
            repository.findById(id).orElseThrow {
                throw ResponseStatusException(NOT_FOUND, "Could not find model $id")
            }
        )
    }
}