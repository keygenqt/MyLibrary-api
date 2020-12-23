package com.keygenqt.mylibrary.base

import com.keygenqt.mylibrary.base.BaseMessageUtils.getMessage
import com.keygenqt.mylibrary.models.Book
import com.keygenqt.mylibrary.models.User
import org.apache.commons.validator.routines.EmailValidator
import org.apache.commons.validator.routines.UrlValidator
import org.springframework.validation.Errors
import java.util.*
import kotlin.reflect.full.memberProperties

fun Errors.validateNickname(model: Any, field: String = "nickname") {
    findValue(field, model)?.let { value ->
        when {
            value.trim().length < 2 -> {
                "field.min.length".let { rejectValue(field, it, getMessage(it, 2)) }
            }
            value.trim().length > 30 -> {
                "field.max.length".let { rejectValue(field, it, getMessage(it, 30)) }
            }
        }
    } ?: run {
        "field.required".let { rejectValue(field, it, getMessage(it)) }
    }
}

fun Errors.validateAvatar(model: Any, field: String = "avatar") {
    findValue(field, model)?.let { value ->
        if (!listOf(User.AVATAR_HAPPY,
                        User.AVATAR_SURPRISED,
                        User.AVATAR_TIRED,
                        User.AVATAR_UPSET,
                        User.AVATAR_OVERWHELMED,
                        User.AVATAR_DEER,
                        User.AVATAR_ENAMORED,
                        User.AVATAR_BIRDIE,
                        User.AVATAR_WHAT,
                        User.AVATAR_SHOCKED,
                        User.AVATAR_TOUCHED,
                        User.AVATAR_ANGRY,
                        User.AVATAR_ZOMBIE,
                        User.AVATAR_PLAYFUL,
                        User.AVATAR_SLEEPY).contains(value)
        ) {
            "field.incorrect".let { rejectValue(field, it, getMessage(it)) }
        }
    } ?: run {
        "field.required".let { rejectValue(field, it, getMessage(it)) }
    }
}

fun Errors.validateCoverType(model: Any, field: String = "coverType") {
    findValue(field, model)?.let { value ->
        if (!listOf(Book.COVER_SOFT,
                        Book.COVER_SOLID,
                        Book.COVER_OTHER).contains(value)
        ) {
            "field.incorrect".let { rejectValue(field, it, getMessage(it)) }
        }
    } ?: run {
        "field.required".let { rejectValue(field, it, getMessage(it)) }
    }
}

fun Errors.validateEmail(model: Any, field: String = "email") {
    findValue(field, model)?.let { value ->
        if (!validatorEmail.isValid(value)) {
            "field.incorrect".let { rejectValue(field, it, getMessage(it)) }
        }
    } ?: run {
        "field.required".let { rejectValue(field, it, getMessage(it)) }
    }
}

fun Errors.validateWebsite(model: Any, field: String = "website") {
    findValue(field, model)?.let { value ->
        if (!urlValidator.isValid(value)) {
            "field.incorrect".let { rejectValue(field, it, getMessage(it)) }
        }
    }
}

fun Errors.validatePassword(model: Any, field: String = "password") {
    findValue(field, model)?.let { value ->
        when {
            value.contains(' ') -> {
                "field.incorrect.spaces".let { rejectValue(field, it, getMessage(it)) }
            }
            value.trim().length < 5 -> {
                "field.min.length".let { rejectValue(field, it, getMessage(it, 5)) }
            }
            value.trim().length > 30 -> {
                "field.max.length".let { rejectValue(field, it, getMessage(it, 30)) }
            }
        }
    } ?: run {
        "field.required".let { rejectValue(field, it, getMessage(it)) }
    }
}

fun Errors.validateYear(model: Any, field: String, required: Boolean = true) {
    findValue(field, model)?.let { value ->
        when {
            value.toIntOrNull() == null -> {
                "field.incorrect".let { rejectValue(field, it, getMessage(it)) }
            }
            value.toInt() > Calendar.getInstance().get(Calendar.YEAR) -> {
                "field.too.mach".let { rejectValue(field, it, getMessage(it)) }
            }
            value.toInt() < 1800 -> {
                "field.too.few".let { rejectValue(field, it, getMessage(it)) }
            }
        }
    } ?: run {
        if (required) {
            "field.required".let { rejectValue(field, it, getMessage(it)) }
        }
    }
}

fun Errors.validateIsInt(model: Any, field: String, required: Boolean = true) {
    findValue(field, model)?.let { value ->
        when {
            value.toIntOrNull() == null -> {
                "field.incorrect".let { rejectValue(field, it, getMessage(it)) }
            }
            value.toInt() > 99999999 -> {
                "field.too.mach".let { rejectValue(field, it, getMessage(it)) }
            }
            value.toInt() == 0 -> {
                "field.too.few".let { rejectValue(field, it, getMessage(it)) }
            }
        }
    } ?: run {
        if (required) {
            "field.required".let { rejectValue(field, it, getMessage(it)) }
        }
    }
}

fun Errors.validateText(model: Any, field: String, min: Int, max: Int = 0, required: Boolean = true) {
    findValue(field, model)?.let { value ->
        when {
            max == 0 && value.trim().length != min -> {
                "field.length".let { rejectValue(field, it, getMessage(it, min)) }
            }
            max != 0 && value.trim().length < min -> {
                "field.min.length".let { rejectValue(field, it, getMessage(it, min)) }
            }
            max != 0 && value.trim().length > max -> {
                "field.max.length".let { rejectValue(field, it, getMessage(it, max)) }
            }
        }
    } ?: run {
        if (required) {
            "field.required".let { rejectValue(field, it, getMessage(it)) }
        }
    }
}

fun Errors.validateRequired(model: Any, field: String) {
    val value = findValue(field, model)
    if (findValue(field, model) == null || value == "0") {
        "field.required".let { rejectValue(field, it, getMessage(it)) }
    }
}

private var urlValidator: UrlValidator = UrlValidator.getInstance()
private val validatorEmail: EmailValidator = EmailValidator.getInstance()

private fun findValue(field: String, model: Any): String? {
    model::class.memberProperties.forEach { p ->
        if (p.name == field) {
            val value = p.getter.call(model) as String?
            return if (value.isNullOrEmpty()) {
                null
            } else {
                value
            }
        }
    }
    return null
}