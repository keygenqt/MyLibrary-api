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

package com.keygenqt.mylibrary.api.controllers;

import com.keygenqt.mylibrary.api.bodies.BookBody;
import com.keygenqt.mylibrary.api.validators.BookValidator;
import com.keygenqt.mylibrary.base.BaseFormatResponse;
import com.keygenqt.mylibrary.base.BaseMessageUtils;
import com.keygenqt.mylibrary.base.JWTAuthorizationFilter;
import com.keygenqt.mylibrary.models.Book;
import com.keygenqt.mylibrary.models.assemblers.BookAssembler;
import com.keygenqt.mylibrary.models.repositories.BookRepository;
import com.keygenqt.mylibrary.models.repositories.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RepositoryRestController
public class BookController {

    private final UserTokenRepository userTokenRepository;

    private final BookRepository bookRepository;

    private final BookAssembler bookAssembler;

    private final BookValidator bookValidator;

    @Autowired
    public BookController(UserTokenRepository userTokenRepository, BookRepository bookRepository, BookAssembler bookAssembler, BookValidator bookValidator) {
        this.userTokenRepository = userTokenRepository;
        this.bookRepository = bookRepository;
        this.bookAssembler = bookAssembler;
        this.bookValidator = bookValidator;
    }

    @PostMapping(path = "/books")
    ResponseEntity<Object> add(@RequestBody BookBody model, BindingResult bindingResult, HttpServletRequest request) {

        bookValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            return BaseFormatResponse.getErrorFormat(bindingResult);
        } else {

            var token = userTokenRepository.findByToken(request.getHeader(JWTAuthorizationFilter.HEADER));

            if (token == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error add book");
            }

            var book = new Book(
                    Long.parseLong(model.getGenreId()),
                    token.getUserId(),
                    model.getImage(),
                    model.getTitle(),
                    model.getAuthor(),
                    model.getPublisher(),
                    model.getISBN(),
                    model.getYear(),
                    model.getNumberOfPages(),
                    model.getDescription(),
                    model.getCoverType(),
                    Boolean.parseBoolean(model.getSale())
            );

            bookRepository.save(book);

            return new ResponseEntity<>(bookAssembler.toModel(book), HttpStatus.OK);
        }
    }

    @PutMapping(path = "/books/{id}")
    ResponseEntity<Object> update(@PathVariable Long id, @RequestBody BookBody model, BindingResult bindingResult, HttpServletRequest request) {

        bookValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            return BaseFormatResponse.getErrorFormat(bindingResult);
        } else {

            var token = userTokenRepository.findByToken(request.getHeader(JWTAuthorizationFilter.HEADER));

            if (token == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error update book 1");
            }

            var book = bookRepository.findById(id).orElse(null);

            if (book == null || !token.getUserId().equals(book.getUserId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error update book 2");
            }

            book.setGenreId(Long.parseLong(model.getGenreId()));
            book.setUserId(token.getUserId());
            book.setImage(model.getImage());
            book.setTitle(model.getTitle());
            book.setAuthor(model.getAuthor());
            book.setPublisher(model.getPublisher());
            book.setIsbn(model.getISBN());
            book.setYear(model.getYear());
            book.setNumberOfPages(model.getNumberOfPages());
            book.setDescription(model.getDescription());
            book.setCoverType(model.getCoverType());
            book.setSale(Boolean.parseBoolean(model.getSale()));

            bookRepository.save(book);

            return new ResponseEntity<>(bookAssembler.toModel(book), HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/books/{id}")
    ResponseEntity<Object> delete(@PathVariable Long id, HttpServletRequest request) {

        var token = userTokenRepository.findByToken(request.getHeader(JWTAuthorizationFilter.HEADER));

        if (token == null || !token.getUserId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error delete book");
        }

        var book = bookRepository.findById(id).orElse(null);

        if (book != null) {
            book.setEnabled(false);
            bookRepository.save(book);
            return BaseFormatResponse.getSuccessFormat(BaseMessageUtils.getMessage("message.delete", "Object"));
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error delete book");
    }
}
