package com.keygenqt.mylibrary.books

import org.springframework.hateoas.*
import org.springframework.hateoas.server.mvc.*
import org.springframework.web.bind.annotation.*

@RestController
internal class BookController(private val repository: BookRepository) {

    // Aggregate root
    @GetMapping("/books")
    fun all(): CollectionModel<EntityModel<Book>> {
        val models: List<EntityModel<Book>> = repository.findAll()
            .map { model: Book ->
                EntityModel.of(model,
                    linkTo<BookController> { one(model.id!!) }.withSelfRel(),
                    linkTo<BookController> { all() }.withRel("books"))
            }
        return CollectionModel.of(models, linkTo<BookController> { all() }.withSelfRel())
    }

    @PostMapping("/books")
    fun newBook(@RequestBody newBook: Book): Book {
        return repository.save(newBook)
    }

    @PutMapping("/books/{id}")
    fun replaceBook(@RequestBody newBook: Book, @PathVariable id: Long): Book {
        return repository.findById(id)
            .map { book ->
                book.title = newBook.title
                repository.save(book)
            }
            .orElseGet {
                newBook.id = id
                repository.save(newBook)
            }
    }

    @DeleteMapping("/books/{id}")
    fun deleteBook(@PathVariable id: Long) {
        repository.deleteById(id)
    }

    @GetMapping("/books/{id}") fun one(@PathVariable id: Long): EntityModel<Book> {
        val model: Book = repository.findById(id).orElseThrow<RuntimeException> { BookNotFoundException(id) }
        return EntityModel.of(model,
            linkTo<BookController> { one(id) }.withSelfRel(),
            linkTo<BookController> { all() }.withRel("books"))
    }

}