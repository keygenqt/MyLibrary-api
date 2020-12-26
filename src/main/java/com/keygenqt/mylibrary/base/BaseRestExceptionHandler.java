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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BaseRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            Exception.class,
            IllegalArgumentException.class,
            ResponseStatusException.class
    })
    ResponseEntity<ErrorResponse> springHandle(Exception ex) {

        var status = HttpStatus.BAD_REQUEST.value();
        var error = HttpStatus.BAD_REQUEST;
        var message = ex.getMessage() == null ? "Bad Request" : ex.getMessage();

        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("404")) {
                status = HttpStatus.NOT_FOUND.value();
                error = HttpStatus.NOT_FOUND;
                var messages = ex.getMessage().split("\"");
                message = messages.length == 0 ? "Not found" : messages[messages.length - 1];
            }
        }
        ErrorResponse errorResponse = new ErrorResponse(
                status,
                error,
                message
        );
        return new ResponseEntity<>(errorResponse, errorResponse.getError());
    }
}
