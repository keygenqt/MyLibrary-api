package com.keygenqt.mylibrary.base

import org.springframework.web.servlet.i18n.*
import java.util.*
import javax.servlet.http.*

class BaseLocaleResolver : AcceptHeaderLocaleResolver() {
    override fun resolveLocale(request: HttpServletRequest): Locale {
        if (request.getHeader("Accept-Language").isNullOrBlank()) {
            return Locale.ENGLISH
        }
        return Locale.Builder().setLanguageTag(request.getHeader("Accept-Language")).build()
    }
}