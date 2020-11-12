package com.keygenqt.mylibrary.api.validators

import com.keygenqt.mylibrary.models.*
import com.keygenqt.mylibrary.models.repositories.*
import org.springframework.beans.factory.annotation.*
import org.springframework.context.*
import org.springframework.security.crypto.bcrypt.*
import org.springframework.stereotype.*
import org.springframework.validation.*
import java.util.*

@Component
class LoginValidator : Validator {

    @Autowired
    private lateinit var messageSource: MessageSource

    @Autowired
    private lateinit var repository: UserRepository

    override fun supports(clazz: Class<*>): Boolean {
        return User::class.java == clazz
    }

    override fun validate(target: Any, errors: Errors) {

        ValidationUtils.rejectIfEmpty(errors, "email", "field.required",
            messageSource.getMessage("field.required", arrayOf("Email"), Locale.ENGLISH))

        ValidationUtils.rejectIfEmpty(errors, "password", "field.required",
            messageSource.getMessage("field.required", arrayOf("Password"), Locale.ENGLISH))

        ValidationUtils.rejectIfEmpty(errors, "uid", "field.required",
            messageSource.getMessage("field.required", arrayOf("UID"), Locale.ENGLISH))

        if (target is Login) {
            target.password?.let {
                if (it.trim().isNotEmpty()) {
                    if (it.trim().length < 5) {
                        errors.rejectValue("password", "field.min.length",
                            messageSource.getMessage("field.min.length", arrayOf("Password", 5), Locale.ENGLISH))
                    }
                    if (it.trim().length > 10) {
                        errors.rejectValue("password", "field.max.length",
                            messageSource.getMessage("field.max.length", arrayOf("Password", 30), Locale.ENGLISH))
                    }
                }
            }

            if (!errors.hasErrors()) {
                repository.findAllByEmail(target.email ?: "")?.let {
                    if (BCryptPasswordEncoder().matches(target.password, it.password)) {

                    } else {
                        errors.rejectValue("password", "field.incorrect",
                            messageSource.getMessage("field.incorrect", arrayOf("password"), Locale.ENGLISH))
                    }
                } ?: run {
                    errors.rejectValue("email", "found.empty",
                        messageSource.getMessage("found.empty", arrayOf("Email"), Locale.ENGLISH))
                }
            }
        }
    }
}