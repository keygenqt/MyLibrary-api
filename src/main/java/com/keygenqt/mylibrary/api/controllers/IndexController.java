package com.keygenqt.mylibrary.api.controllers;

import com.keygenqt.mylibrary.api.assemblers.IndexResourceAssembler;
import com.keygenqt.mylibrary.api.resources.IndexResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
class IndexController {

    private final IndexResourceAssembler assembler;

    @Autowired
    public IndexController(IndexResourceAssembler assembler) {
        this.assembler = assembler;
    }

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<IndexResource> index() {
        return ResponseEntity.ok(assembler.buildIndex());
    }
}
