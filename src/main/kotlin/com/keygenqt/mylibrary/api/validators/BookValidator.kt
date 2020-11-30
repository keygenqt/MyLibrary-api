package com.keygenqt.mylibrary.api.validators

import com.keygenqt.mylibrary.base.*
import com.keygenqt.mylibrary.models.repositories.*
import org.springframework.beans.factory.annotation.*
import org.springframework.data.repository.*
import org.springframework.stereotype.*
import org.springframework.validation.*

@Component
class BookValidator : Validator {

    @Autowired
    private lateinit var repository: GenreRepository

    override fun supports(clazz: Class<*>): Boolean {
        return BookBody::class.java == clazz
    }

    override fun validate(target: Any, errors: Errors) {

        errors.validateText(target, "title", 2, 250)
        errors.validateText(target, "author", 2, 250)
        errors.validateText(target, "description", 2, 5000)
        errors.validateText(target, "publisher", 2, 250)
        errors.validateYear(target, "year")
        errors.validateText(target, "ISBN", 17)
        errors.validateIsInt(target, "numberOfPages")
        errors.validateCoverType(target)
        errors.validateRequired(target, "genreId")

        if (!errors.hasErrors() && target is BookBody) {
            if (repository.findByIdOrNull(target.genreId?.toLongOrNull() ?: 0) == null) {
                "field.found.empty".let { errors.rejectValue("genreId", it, BaseMessageUtils.getMessage(it)) }
            }
        }
    }
}