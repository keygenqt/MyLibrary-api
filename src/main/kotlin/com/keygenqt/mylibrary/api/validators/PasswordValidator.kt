package com.keygenqt.mylibrary.api.validators

import org.springframework.beans.factory.annotation.*
import org.springframework.context.*
import org.springframework.stereotype.*
import org.springframework.validation.*
import java.util.*

@Component
class PasswordValidator : Validator {

    @Autowired
    private lateinit var messageSource: MessageSource

    override fun supports(clazz: Class<*>): Boolean {
        return Password::class.java == clazz
    }

    override fun validate(target: Any, errors: Errors) {

        ValidationUtils.rejectIfEmpty(errors, "password", "field.required",
            messageSource.getMessage("field.required", arrayOf("Password"), Locale.ENGLISH))

        ValidationUtils.rejectIfEmpty(errors, "rpassword", "field.required",
            messageSource.getMessage("field.required", arrayOf("Confirm Password"), Locale.ENGLISH))

        if (target is Password) {
            target.password?.let {
                if (it.trim().isNotEmpty()) {
                    if (it.trim().length < 5) {
                        errors.rejectValue("password", "field.min.length",
                            messageSource.getMessage("field.min.length", arrayOf("Password", 5), Locale.ENGLISH))
                    }
                    if (it.trim().length > 30) {
                        errors.rejectValue("password", "field.max.length",
                            messageSource.getMessage("field.max.length", arrayOf("Password", 30), Locale.ENGLISH))
                    }
                }
            }
            if (target.password != null && target.rpassword != null) {
                if (target.password != target.rpassword) {
                    errors.rejectValue("rpassword", "field.match",
                        messageSource.getMessage("field.match", arrayOf("password"), Locale.ENGLISH))
                }
            }
        }
    }
}