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
