package com.keygenqt.mylibrary.base

import org.springframework.context.*
import org.springframework.context.i18n.*
import org.springframework.stereotype.*

@Component
class BaseMessageUtils : MessageSourceAware {
    override fun setMessageSource(messageSource: MessageSource) {
        source = messageSource
    }

    companion object {
        private lateinit var source: MessageSource

        fun getMessage(key: String, vararg params: Any): String {
            return source.getMessage(key, params, LocaleContextHolder.getLocale())
        }
    }
}