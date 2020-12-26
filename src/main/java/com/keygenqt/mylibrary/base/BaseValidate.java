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

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.IntegerValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.validation.Errors;

import java.util.Calendar;

public class BaseValidate {

    public final static String MESSAGE_FIELD_REQUIRED = "field.required";
    public final static String MESSAGE_FIELD_INCORRECT = "field.incorrect";
    public final static String MESSAGE_FIELD_TOO_MACH = "field.too.mach";
    public final static String MESSAGE_FIELD_TOO_FEW = "field.too.few";
    public final static String MESSAGE_FIELD_MIN_LENGTH = "field.min.length";
    public final static String MESSAGE_FIELD_MAX_LENGTH = "field.max.length";
    public final static String MESSAGE_FIELD_INCORRECT_SPACES = "field.incorrect.spaces";
    public final static String MESSAGE_FIELD_MATCH = "field.match";
    public final static String MESSAGE_ALREADY_TAKEN = "field.already.taken";
    public final static String MESSAGE_FOUND_EMPTY = "field.found.empty";

    public static UrlValidator validatorUrl = UrlValidator.getInstance();
    public static EmailValidator validatorEmail = EmailValidator.getInstance();
    public static IntegerValidator validatorInteger = IntegerValidator.getInstance();

    public static void setError(String name, String message, Errors errors, Object... params) {
        errors.rejectValue(name, message, BaseMessageUtils.getMessage(message, params));
    }

    protected static String findValue(String name, Object model) {
        try {
            var field = model.getClass().getDeclaredField(name);
            field.setAccessible(true);
            var value = (String) field.get(model);
            if (value != null && value.equals("")) {
                return null;
            }
            return value;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isRequired(String name, Object model, Errors errors) {
        var value = findValue(name, model);
        if (value == null || value.equals("0")) {
            setError(name, MESSAGE_FIELD_REQUIRED, errors);
            return false;
        }
        return true;
    }

    public static boolean isEmail(String name, Object model, Errors errors) {
        var value = findValue(name, model);
        if (value != null && !validatorEmail.isValid(value)) {
            setError(name, MESSAGE_FIELD_INCORRECT, errors);
            return false;
        }
        return true;
    }

    public static boolean isWebsite(String name, Object model, Errors errors) {
        var value = findValue(name, model);
        if (value != null && !validatorUrl.isValid(value)) {
            setError(name, MESSAGE_FIELD_INCORRECT, errors);
            return false;
        }
        return true;
    }

    public static boolean isYear(String name, Object model, Errors errors) {
        var value = findValue(name, model);
        if (value != null) {
            if (!validatorInteger.isValid(value)) {
                setError(name, MESSAGE_FIELD_INCORRECT, errors);
            } else {
                int i = Integer.parseInt(value);
                if (i > Calendar.getInstance().get(Calendar.YEAR)) {
                    setError(name, MESSAGE_FIELD_TOO_MACH, errors);
                } else if (i < 1000) {
                    setError(name, MESSAGE_FIELD_TOO_FEW, errors);
                }
            }
        }
        return true;
    }

    public static boolean isInt(String name, Object model, Errors errors) {
        var value = findValue(name, model);
        if (value != null) {
            if (!validatorInteger.isValid(value)) {
                setError(name, MESSAGE_FIELD_INCORRECT, errors);
            } else {
                int i = Integer.parseInt(value);
                if (i > 99999999) {
                    setError(name, MESSAGE_FIELD_TOO_MACH, errors);
                } else if (i < 1) {
                    setError(name, MESSAGE_FIELD_TOO_FEW, errors);
                }
            }
        }
        return true;
    }

    public static boolean isTextLength(String name, Integer min, Integer max, Object model, Errors errors) {
        var value = findValue(name, model);
        if (value != null) {
            if (value.length() < min) {
                setError(name, MESSAGE_FIELD_MIN_LENGTH, errors, min);
                return false;
            } else if (value.length() > max) {
                setError(name, MESSAGE_FIELD_MAX_LENGTH, errors, max);
                return false;
            }
        }
        return true;
    }

    public static boolean isMatch(String name1, String name2, Object model, Errors errors) {
        var value1 = findValue(name1, model);
        var value2 = findValue(name2, model);
        if (value1 != null && value2 != null && !value1.equals(value2)) {
            setError(name2, MESSAGE_FIELD_MATCH, errors);
        }
        return true;
    }
}
