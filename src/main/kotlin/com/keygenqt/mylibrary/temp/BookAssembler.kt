package com.keygenqt.mylibrary.temp //package com.keygenqt.mylibrary.temp.books_custom_ex
//
//import org.springframework.hateoas.*
//import org.springframework.hateoas.server.*
//import org.springframework.hateoas.server.mvc.*
//import org.springframework.stereotype.*
//
//@Component
//internal class BookAssembler : RepresentationModelAssembler<Book, EntityModel<Book>> {
//    override fun toModel(model: Book): EntityModel<Book> {
//        return EntityModel.of<Book>(model,
//            linkTo<BookController> { one(model.id!!) }.withSelfRel(),
//            linkTo<BookController> { all() }.withRel("books"))
//    }
//}