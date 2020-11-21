package com.keygenqt.mylibrary.api.validators

import com.keygenqt.mylibrary.base.*
import com.keygenqt.mylibrary.models.repositories.*
import org.springframework.beans.factory.annotation.*
import org.springframework.security.crypto.bcrypt.*
import org.springframework.stereotype.*
import org.springframework.validation.*

@Component
class LoginValidator : Validator {

    @Autowired
    private lateinit var repository: UserRepository

    override fun supports(clazz: Class<*>): Boolean {
        return Login::class.java == clazz
    }

    override fun validate(target: Any, errors: Errors) {

        errors.validateEmail(target)
        errors.validatePassword(target)
        errors.validateRequired(target, "uid")

        if (!errors.hasErrors() && target is Login) {
            repository.findAllByEmail(target.email ?: "")?.let { model ->
                if (!BCryptPasswordEncoder().matches(target.password, model.password)) {
                    "field.incorrect".let { errors.rejectValue("password", it, BaseMessageUtils.getMessage(it)) }
                }
            } ?: run {
                "field.found.empty".let { errors.rejectValue("email", it, BaseMessageUtils.getMessage(it)) }
            }
        }
    }
}