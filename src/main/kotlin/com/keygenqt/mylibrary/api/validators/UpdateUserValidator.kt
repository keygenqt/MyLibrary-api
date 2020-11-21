package com.keygenqt.mylibrary.api.validators

import com.keygenqt.mylibrary.models.*
import com.keygenqt.mylibrary.models.repositories.*
import org.apache.commons.validator.routines.*
import org.springframework.beans.factory.annotation.*
import org.springframework.context.*
import org.springframework.stereotype.*
import org.springframework.validation.*
import java.util.*

@Component
class UpdateUserValidator : Validator {

    var urlValidator: UrlValidator = UrlValidator.getInstance()

    @Autowired
    private lateinit var messageSource: MessageSource

    override fun supports(clazz: Class<*>): Boolean {
        return UpdateUser::class.java == clazz
    }

    override fun validate(target: Any, errors: Errors) {

        ValidationUtils.rejectIfEmpty(errors, "nickname", "field.required",
            messageSource.getMessage("field.required", arrayOf("Nickname"), Locale.ENGLISH))

        ValidationUtils.rejectIfEmpty(errors, "avatar", "field.required",
            messageSource.getMessage("field.required", arrayOf("Avatar"), Locale.ENGLISH))

        if (target is UpdateUser) {
            target.nickname?.let {
                if (it.trim().isNotEmpty()) {
                    if (it.trim().length < 2) {
                        errors.rejectValue("nickname", "field.min.length",
                            messageSource.getMessage("field.min.length", arrayOf("Nickname", 5), Locale.ENGLISH))
                    }
                    if (it.trim().length > 30) {
                        errors.rejectValue("nickname", "field.max.length",
                            messageSource.getMessage("field.max.length", arrayOf("Nickname", 30), Locale.ENGLISH))
                    }
                }
            }
            target.website?.let {
                if (it.isNotEmpty() && !urlValidator.isValid(it)) {
                    errors.rejectValue("website", "field.incorrect",
                        messageSource.getMessage("field.incorrect", arrayOf("website"), Locale.ENGLISH))
                }
            }
        }
    }
}