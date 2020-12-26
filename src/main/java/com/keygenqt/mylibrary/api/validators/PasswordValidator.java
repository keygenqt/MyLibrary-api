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

package com.keygenqt.mylibrary.api.validators;

import com.keygenqt.mylibrary.api.bodies.PasswordBody;
import com.keygenqt.mylibrary.base.CustomValidate;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PasswordValidator implements Validator {

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return PasswordBody.class == clazz;
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        CustomValidate.isPassword("password", target, errors);
        CustomValidate.isRequired("rpassword", target, errors);
        CustomValidate.isMatch("password", "rpassword", target, errors);
    }
}
