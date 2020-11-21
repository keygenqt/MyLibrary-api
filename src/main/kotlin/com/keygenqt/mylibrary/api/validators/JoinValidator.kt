package com.keygenqt.mylibrary.api.validators

import com.keygenqt.mylibrary.base.*
import com.keygenqt.mylibrary.models.repositories.*
import org.springframework.beans.factory.annotation.*
import org.springframework.stereotype.*
import org.springframework.validation.*

@Component
class JoinValidator : Validator {

    @Autowired
    private lateinit var repository: UserRepository

    override fun supports(clazz: Class<*>): Boolean {
        return Join::class.java == clazz
    }

    override fun validate(target: Any, errors: Errors) {

        errors.validateNickname(target)
        errors.validateEmail(target)
        errors.validatePassword(target)
        errors.validateRequired(target, "uid")
        errors.validateAvatar(target)

        if (!errors.hasErrors() && target is Join) {
            repository.findAllByEmail(target.email ?: "")?.let {
                "field.already.taken".let { errors.rejectValue("email", it, BaseMessageUtils.getMessage(it)) }
            }
        }
    }
}