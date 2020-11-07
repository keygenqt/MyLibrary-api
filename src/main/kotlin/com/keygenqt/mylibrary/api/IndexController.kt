package com.keygenqt.mylibrary.api

import com.keygenqt.mylibrary.api.resources.*
import org.springframework.beans.factory.annotation.*
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.*

@RestController
@RequestMapping("/")
class IndexController @Autowired constructor(private val indexResourceAssembler: IndexResourceAssembler) {
    @RequestMapping(method = [GET]) fun index(): ResponseEntity<IndexResource> {
        return ResponseEntity.ok(indexResourceAssembler.buildIndex())
    }
}