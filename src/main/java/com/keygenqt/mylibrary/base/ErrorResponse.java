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

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime datetime;

    Integer status;

    HttpStatus error;

    String message;

    public ErrorResponse(Integer status, HttpStatus error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        datetime = LocalDateTime.now();
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public Integer getStatus() {
        return status;
    }

    public HttpStatus getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
