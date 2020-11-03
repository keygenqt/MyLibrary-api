package com.keygenqt.mylibrary.books

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
internal class BookController {

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var bookModelAssembler: BookAssembler

    @Autowired
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    private lateinit var pagedResourcesAssembler: PagedResourcesAssembler<Book>

    @GetMapping(path = ["/books"]) fun all(
        @PageableDefault(page = 0, size = 20)
        @SortDefaults(SortDefault(sort = ["title"], direction = Sort.Direction.DESC), SortDefault(sort = ["id"], direction = Sort.Direction.ASC))
        pageable: Pageable = Pageable.unpaged()
    ): ResponseEntity<PagedModel<EntityModel<Book>>> {
        val collModel = pagedResourcesAssembler
            .toModel<EntityModel<Book>>(bookRepository.findAll(pageable), bookModelAssembler)
        return ResponseEntity(collModel, OK)
    }

    @GetMapping("/books/{id}") fun one(@PathVariable id: Long): EntityModel<Book> {
        return bookModelAssembler.toModel(
            bookRepository.findById(id).orElseThrow {
                throw ResponseStatusException(NOT_FOUND, "Could not find book $id")
            }
        )
    }

    @PostMapping("/books") fun newBook(@RequestBody newEmployee: Book): ResponseEntity<*> {
        val entityModel: EntityModel<Book> = bookModelAssembler.toModel(bookRepository.save(newEmployee))
        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel)
    }

    @PutMapping("/books/{id}")
    fun replaceBook(@RequestBody newBook: Book, @PathVariable id: Long): ResponseEntity<*> {
        val updated = bookRepository.findById(id)
            .map { book ->
                book.title = newBook.title
                bookRepository.save(book)
            }
            .orElseGet {
                newBook.id = id
                bookRepository.save(newBook)
            }
        val entityModel: EntityModel<Book> = bookModelAssembler.toModel(updated)
        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel)
    }

    @DeleteMapping("/books/{id}") fun deleteBook(@PathVariable id: Long): ResponseEntity<*> {
        bookRepository.deleteById(id)
        return ResponseEntity.noContent().build<Any>()
    }
}