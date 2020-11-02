package com.keygenqt.mylibrary.books

import org.springframework.beans.factory.annotation.*
import org.springframework.data.domain.*
import org.springframework.data.web.*
import org.springframework.data.web.SortDefault.*
import org.springframework.hateoas.*
import org.springframework.hateoas.server.mvc.*
import org.springframework.http.*
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*

@RestController
internal class BookController {

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var bookModelAssembler: BookModelAssembler

    @Autowired
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    private lateinit var pagedResourcesAssembler: PagedResourcesAssembler<Book>

    @GetMapping(path = ["/books"]) fun all(
        @PageableDefault(page = 0, size = 20)
        @SortDefaults(SortDefault(sort = ["title"], direction = Sort.Direction.DESC), SortDefault(sort = ["id"], direction = Sort.Direction.ASC))
        pageable: Pageable
    ): ResponseEntity<PagedModel<EntityModel<Book>>> {
        val collModel = pagedResourcesAssembler
            .toModel<EntityModel<Book>>(bookRepository.findAll(pageable), bookModelAssembler)
        return ResponseEntity(collModel, OK)
    }

    @PostMapping("/books")
    fun newBook(@RequestBody newBook: Book): Book {
        return bookRepository.save(newBook)
    }

    @PutMapping("/books/{id}")
    fun replaceBook(@RequestBody newBook: Book, @PathVariable id: Long): Book {
        return bookRepository.findById(id)
            .map { book ->
                book.title = newBook.title
                bookRepository.save(book)
            }
            .orElseGet {
                newBook.id = id
                bookRepository.save(newBook)
            }
    }

    @DeleteMapping("/books/{id}")
    fun deleteBook(@PathVariable id: Long) {
        bookRepository.deleteById(id)
    }

    @GetMapping("/books/{id}") fun one(@PathVariable id: Long): EntityModel<Book> {
        val model = bookRepository.findById(id).orElseThrow<RuntimeException> { BookNotFoundException(id) }
        return bookModelAssembler.toModel(model)
    }

}