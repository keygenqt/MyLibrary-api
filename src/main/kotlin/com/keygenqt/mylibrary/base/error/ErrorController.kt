/*
 * Copyright 2020 Vitaliy Zarubin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keygenqt.mylibrary.base.error

import org.springframework.boot.autoconfigure.web.servlet.error.*
import org.springframework.boot.web.servlet.error.*
import org.springframework.http.*
import org.springframework.stereotype.*
import org.springframework.web.bind.annotation.*
import java.time.*
import javax.servlet.*
import javax.servlet.http.*

@Controller
@RequestMapping("\${server.error.path:\${error.path:/error}}")
class ErrorController(errorAttributes: ErrorAttributes?) : AbstractErrorController(errorAttributes) {

    @RequestMapping
    fun handleError(ex: Exception, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val status: HttpStatus = getStatus(request)
        val message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE).toString().let {
            return@let if (it.isBlank()) {
                "Bad request"
            } else {
                it
            }
        }
        val error = ErrorResponse(
            datetime = LocalDateTime.now(),
            status = status.value(),
            error = status,
            message = ex.message ?: message
        )
        return ResponseEntity(error, error.error)
    }

    override fun getErrorPath(): String {
        return "/error"
    }
}