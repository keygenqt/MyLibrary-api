package com.keygenqt.mylibrary.api.validators

import com.keygenqt.mylibrary.base.*
import org.springframework.stereotype.*
import org.springframework.validation.*

@Component
class UpdateUserValidator : Validator {

    override fun supports(clazz: Class<*>): Boolean {
        return UpdateUserBody::class.java == clazz
    }

    override fun validate(target: Any, errors: Errors) {
        errors.validateNickname(target)
        errors.validateWebsite(target)
    }
}