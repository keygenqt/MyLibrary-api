package com.keygenqt.mylibrary.common

import org.springframework.data.web.*
import org.springframework.data.web.SortDefault.*
import org.springframework.hateoas.*
import org.springframework.web.util.*

inline fun <reified C> Link.getUrlDefaultParamsPageableController(methodName: String): Link {
    return Link.of(this.href.getUrlDefaultParamsPageableController(C::class.java, methodName), this.rel)
}

fun String.getUrlDefaultParamsPageableController(clazz: Class<*>, methodName: String): String {
    val url = UriComponentsBuilder.fromUriString(this)
    clazz.declaredMethods.first { it.name == methodName }.parameters.forEach {
        it.getAnnotation(PageableDefault::class.java)?.let { an ->
            url.queryParam("page", an.page)
            url.queryParam("size", an.size)
        }
        it.getAnnotation(SortDefaults::class.java)?.value?.forEach { an ->
            val params = an.sort.toMutableList()
            params.add(an.direction.name.toLowerCase())
            url.queryParam("sort", params.joinToString(","))
        }
    }
    return url.build().toUriString()
}