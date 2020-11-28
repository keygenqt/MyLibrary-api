package com.keygenqt.mylibrary.base

import com.keygenqt.mylibrary.base.BaseMessageUtils.Companion.getMessage
import com.keygenqt.mylibrary.models.Book.Companion.COVER_SOFT
import com.keygenqt.mylibrary.models.Book.Companion.COVER_SOLID
import com.keygenqt.mylibrary.models.Book.Companion.COVER_UNKNOWN
import com.keygenqt.mylibrary.models.User.Companion.AVATAR_ANGRY
import com.keygenqt.mylibrary.models.User.Companion.AVATAR_BIRDIE
import com.keygenqt.mylibrary.models.User.Companion.AVATAR_DEER
import com.keygenqt.mylibrary.models.User.Companion.AVATAR_ENAMORED
import com.keygenqt.mylibrary.models.User.Companion.AVATAR_HAPPY
import com.keygenqt.mylibrary.models.User.Companion.AVATAR_OVERWHELMED
import com.keygenqt.mylibrary.models.User.Companion.AVATAR_PLAYFUL
import com.keygenqt.mylibrary.models.User.Companion.AVATAR_SHOCKED
import com.keygenqt.mylibrary.models.User.Companion.AVATAR_SLEEPY
import com.keygenqt.mylibrary.models.User.Companion.AVATAR_SURPRISED
import com.keygenqt.mylibrary.models.User.Companion.AVATAR_TIRED
import com.keygenqt.mylibrary.models.User.Companion.AVATAR_TOUCHED
import com.keygenqt.mylibrary.models.User.Companion.AVATAR_UPSET
import com.keygenqt.mylibrary.models.User.Companion.AVATAR_WHAT
import com.keygenqt.mylibrary.models.User.Companion.AVATAR_ZOMBIE
import org.apache.commons.validator.routines.*
import org.springframework.validation.*
import java.util.*
import kotlin.reflect.full.*

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
        if (!listOf(AVATAR_HAPPY,
                AVATAR_SURPRISED,
                AVATAR_TIRED,
                AVATAR_UPSET,
                AVATAR_OVERWHELMED,
                AVATAR_DEER,
                AVATAR_ENAMORED,
                AVATAR_BIRDIE,
                AVATAR_WHAT,
                AVATAR_SHOCKED,
                AVATAR_TOUCHED,
                AVATAR_ANGRY,
                AVATAR_ZOMBIE,
                AVATAR_PLAYFUL,
                AVATAR_SLEEPY).contains(value)
        ) {
            "field.incorrect".let { rejectValue(field, it, getMessage(it)) }
        }
    } ?: run {
        "field.required".let { rejectValue(field, it, getMessage(it)) }
    }
}

fun Errors.validateCoverType(model: Any, field: String = "coverType") {
    findValue(field, model)?.let { value ->
        if (!listOf(COVER_SOFT,
                COVER_SOLID,
                COVER_UNKNOWN).contains(value)
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

fun Errors.validateText(model: Any, field: String, max: Int, min: Int = max, required: Boolean = true) {
    findValue(field, model)?.let { value ->
        when {
            max == max && value.trim().length != min -> {
                "field.length".let { rejectValue(field, it, getMessage(it, min)) }
            }
            value.trim().length < min -> {
                "field.min.length".let { rejectValue(field, it, getMessage(it, min)) }
            }
            value.trim().length > max -> {
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
    if (findValue(field, model) == null) {
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