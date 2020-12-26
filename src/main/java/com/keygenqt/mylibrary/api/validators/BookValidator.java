package com.keygenqt.mylibrary.api.validators;

import com.keygenqt.mylibrary.api.bodies.BookBody;
import com.keygenqt.mylibrary.base.CustomValidate;
import com.keygenqt.mylibrary.models.repositories.GenreRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {

    private final GenreRepository repository;

    @Autowired
    public BookValidator(GenreRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return BookBody.class == clazz;
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {

        CustomValidate.isCoverType("coverType", target, errors);
        CustomValidate.isRequired("genreId", target, errors);
        CustomValidate.isRequired("image", target, errors);
        CustomValidate.isRequired("title", target, errors);
        CustomValidate.isTextLength("title", 2, 250, target, errors);
        CustomValidate.isTextLength("description", 2, 5000, target, errors);
        CustomValidate.isTextLength("author", 2, 250, target, errors);
        CustomValidate.isTextLength("publisher", 2, 250, target, errors);
        CustomValidate.isYear("year", target, errors);
        CustomValidate.isInt("numberOfPages", target, errors);

        if (!errors.hasErrors()) {
            var body = (BookBody) target;
            var genre = repository.findById(Long.parseLong(body.getGenreId())).orElse(null);
            if (genre == null) {
                CustomValidate.setError("genreId", CustomValidate.MESSAGE_FIELD_INCORRECT, errors);
            }
        }
    }
}
