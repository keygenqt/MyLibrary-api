package com.keygenqt.mylibrary.api.validators;

import com.keygenqt.mylibrary.api.bodies.UpdateUserBody;
import com.keygenqt.mylibrary.base.CustomValidate;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UpdateUserMessageTokenValidator implements Validator {

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return UpdateUserBody.class == clazz;
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        CustomValidate.isRequired("token", target, errors);
    }
}
