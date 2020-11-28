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

import com.keygenqt.mylibrary.models.assemblers.*
import com.keygenqt.mylibrary.models.repositories.*
import org.springframework.beans.factory.annotation.*
import org.springframework.http.*
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.*

@RestController
class BookController {

    @Autowired
    private lateinit var repository: BookRepository

    @Autowired
    private lateinit var assembler: BookAssembler

//    @GetMapping(path = ["/books/{id}"])
//    fun bookById(@PathVariable id: Long): ResponseEntity<Any> {
//        repository.findById(id).orElse(null)?.let { model ->
//            return ResponseEntity(assembler.toModel(model), OK)
//        }
//        throw ResponseStatusException(NOT_FOUND, "Page not found")
//    }
}