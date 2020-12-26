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

import com.keygenqt.mylibrary.api.bodies.LoginBody;
import com.keygenqt.mylibrary.base.CustomValidate;
import com.keygenqt.mylibrary.models.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LoginValidator implements Validator {

    private final UserRepository repository;

    @Autowired
    public LoginValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return LoginBody.class == clazz;
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {

        CustomValidate.isRequired("email", target, errors);
        CustomValidate.isEmail("email", target, errors);
        CustomValidate.isPassword("password", target, errors);
        CustomValidate.isRequired("uid", target, errors);

        if (!errors.hasErrors()) {
            var body = (LoginBody) target;
            var user = repository.findAllByEmail(body.getEmail());
            if (user == null) {
                CustomValidate.setError("email", CustomValidate.MESSAGE_FOUND_EMPTY, errors);
            } else {
                if (!new BCryptPasswordEncoder().matches(body.getPassword(), user.getPassword())) {
                    CustomValidate.setError("password", CustomValidate.MESSAGE_FIELD_INCORRECT, errors);
                }
            }
        }
    }
}
