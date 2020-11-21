package com.keygenqt.mylibrary.api.validators

import com.keygenqt.mylibrary.base.*
import org.springframework.stereotype.*
import org.springframework.validation.*

@Component
class PasswordValidator : Validator {

    override fun supports(clazz: Class<*>): Boolean {
        return Password::class.java == clazz
    }

    override fun validate(target: Any, errors: Errors) {

        errors.validatePassword(target)
        errors.validateRequired(target, "rpassword")

        if (!errors.hasErrors() && target is Password) {
            if (target.password != target.rpassword) {
                "field.match".let { errors.rejectValue("rpassword", it, BaseMessageUtils.getMessage(it)) }
            }
        }
    }
}