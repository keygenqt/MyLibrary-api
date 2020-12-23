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

package com.keygenqt.mylibrary.base;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
class BaseErrorController extends AbstractErrorController {

    public BaseErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping
    ResponseEntity<ErrorResponse> handleError(Exception ex, HttpServletRequest request) {
        var status = getStatus(request);
        String message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE).toString();
        var error = new ErrorResponse(
                status.value(),
                status,
                message.equals("") ? HttpStatus.BAD_REQUEST.name() : message
        );
        return new ResponseEntity<>(error, error.error);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}