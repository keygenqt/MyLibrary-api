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
