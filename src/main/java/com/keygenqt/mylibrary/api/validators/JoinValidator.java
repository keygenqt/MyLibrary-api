package com.keygenqt.mylibrary.api.validators;

import com.keygenqt.mylibrary.api.bodies.JoinBody;
import com.keygenqt.mylibrary.base.CustomValidate;
import com.keygenqt.mylibrary.models.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class JoinValidator implements Validator {

    private final UserRepository repository;

    @Autowired
    public JoinValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return JoinBody.class == clazz;
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {

        CustomValidate.isNickname("nickname", target, errors);
        CustomValidate.isRequired("email", target, errors);
        CustomValidate.isEmail("email", target, errors);
        CustomValidate.isPassword("password", target, errors);
        CustomValidate.isRequired("uid", target, errors);
        CustomValidate.isAvatar("avatar", target, errors);

        if (!errors.hasErrors()) {
            var body = (JoinBody) target;
            var user = repository.findAllByEmail(body.getEmail());
            if (user != null) {
                CustomValidate.setError("email", CustomValidate.MESSAGE_ALREADY_TAKEN, errors);
            }
        }
    }
}
