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
import org.springframework.validation.BindingResult;

import java.sql.Timestamp;
import java.util.HashMap;

public class BaseFormatResponse {
    public static ResponseEntity<Object> getErrorFormat(BindingResult result) {
        return new ResponseEntity<>(new HashMap<String, Object>() {{
            put("errors", result.getFieldErrors());
            put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
            put("error", "Validate");
            put("message", "Validation failed");
            put("timestamp", new Timestamp(System.currentTimeMillis()));
        }}, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public static ResponseEntity<Object> getSuccessFormat(String message) {
        return new ResponseEntity<>(new HashMap<String, Object>() {{
            put("status", HttpStatus.OK.value());
            put("message", message);
            put("timestamp", new Timestamp(System.currentTimeMillis()));
        }}, HttpStatus.OK);
    }
}
