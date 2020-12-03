/*
 * Copyright 2020 Vitaliy Zarubin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keygenqt.mylibrary.api

import com.keygenqt.mylibrary.api.validators.*
import com.keygenqt.mylibrary.base.*
import com.keygenqt.mylibrary.extensions.*
import com.keygenqt.mylibrary.models.*
import com.keygenqt.mylibrary.models.assemblers.*
import com.keygenqt.mylibrary.models.repositories.*
import com.keygenqt.mylibrary.security.*
import org.springframework.beans.factory.annotation.*
import org.springframework.data.repository.*
import org.springframework.data.rest.webmvc.*
import org.springframework.http.*
import org.springframework.http.HttpStatus.*
import org.springframework.validation.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.*
import javax.servlet.http.*

@RepositoryRestController
class BookController {

    @Autowired
    private lateinit var repository: BookRepository

    @Autowired
    private lateinit var assembler: BookAssembler

    @Autowired
    private lateinit var updateBookValidator: BookValidator

    @Autowired
    private lateinit var repositoryToken: UserTokenRepository

    @PutMapping(path = ["/books/{id}"])
    fun update(@PathVariable id: Long, @RequestBody model: BookBody, bindingResult: BindingResult): ResponseEntity<Any> {

        updateBookValidator.validate(model, bindingResult)

        if (bindingResult.hasErrors()) {
            return bindingResult.getErrorFormat()
        } else {
            repository.findByIdOrNull(id)?.let { book ->
                book.apply {
                    title = model.title!!
                    genreId = model.genreId?.toLong()!!
                    coverType = model.coverType ?: Book.COVER_OTHER
                    sale = model.sale?.toBoolean() ?: false

                    model.image?.let {
                        image = it
                    }
                    model.publisher?.let {
                        publisher = it
                    }
                    model.year?.let {
                        year = it.toIntOrNull()
                    }
                    model.ISBN?.let {
                        ISBN = it
                    }
                    model.numberOfPages?.let {
                        numberOfPages = it.toIntOrNull()
                    }
                    model.description?.let {
                        description = it
                    }
                }

                repository.save(book)
                return ResponseEntity(assembler.toModel(book), OK)
            }
        }
        throw ResponseStatusException(BAD_REQUEST, "Error update book")
    }

    @PostMapping(path = ["/books"])
    fun add(@RequestBody model: BookBody, bindingResult: BindingResult, request: HttpServletRequest): ResponseEntity<Any> {

        updateBookValidator.validate(model, bindingResult)

        if (bindingResult.hasErrors()) {
            return bindingResult.getErrorFormat()
        } else {
            repositoryToken.findByToken(request.getHeader(JWTAuthorizationFilter.HEADER))?.let { modelToken ->
                val book = Book().apply {
                    title = model.title!!
                    genreId = model.genreId?.toLong()!!
                    userId = modelToken.userId
                    coverType = model.coverType ?: Book.COVER_OTHER
                    sale = model.sale?.toBoolean() ?: false

                    model.image?.let {
                        image = it
                    }
                    model.publisher?.let {
                        publisher = it
                    }
                    model.year?.let {
                        year = it.toIntOrNull()
                    }
                    model.ISBN?.let {
                        ISBN = it
                    }
                    model.numberOfPages?.let {
                        numberOfPages = it.toIntOrNull()
                    }
                    model.description?.let {
                        description = it
                    }
                }
                repository.save(book)
                return ResponseEntity(assembler.toModel(book), OK)
            }
        }
        throw ResponseStatusException(BAD_REQUEST, "Error add book")
    }

    @DeleteMapping(path = ["/books/{id}"])
    fun delete(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<Any> {
        repositoryToken.findByToken(request.getHeader(JWTAuthorizationFilter.HEADER))?.let { modelToken ->
            repository.findByIdOrNull(id)?.let { book ->
                if (book.userId == modelToken.userId) {
                    book.enabled = false
                    repository.save(book)
                    return getSuccessFormat(BaseMessageUtils.getMessage("message.delete", "Object"))
                } else {
                    throw ResponseStatusException(BAD_REQUEST, "Error delete book")
                }
            }
        }
        throw ResponseStatusException(BAD_REQUEST, "Error delete book")
    }
}