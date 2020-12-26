package com.keygenqt.mylibrary.base;

import com.keygenqt.mylibrary.models.Book;
import com.keygenqt.mylibrary.models.User;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.Objects;

public class CustomValidate extends BaseValidate {

    public static boolean isCoverType(String name, Object model, Errors errors) {
        if (isRequired(name, model, errors)) {
            var value = findValue(name, model);
            if (!new ArrayList<String>() {{
                add(Book.COVER_SOFT);
                add(Book.COVER_SOLID);
                add(Book.COVER_OTHER);
            }}.contains(value)) {
                setError(name, MESSAGE_FIELD_INCORRECT, errors);
                return false;
            }
        }
        return true;
    }

    public static boolean isAvatar(String name, Object model, Errors errors) {
        if (isRequired(name, model, errors)) {
            var value = findValue(name, model);
            if (!new ArrayList<String>() {{
                add(User.AVATAR_HAPPY);
                add(User.AVATAR_SURPRISED);
                add(User.AVATAR_TIRED);
                add(User.AVATAR_UPSET);
                add(User.AVATAR_OVERWHELMED);
                add(User.AVATAR_DEER);
                add(User.AVATAR_ENAMORED);
                add(User.AVATAR_BIRDIE);
                add(User.AVATAR_WHAT);
                add(User.AVATAR_SHOCKED);
                add(User.AVATAR_TOUCHED);
                add(User.AVATAR_ANGRY);
                add(User.AVATAR_ZOMBIE);
                add(User.AVATAR_PLAYFUL);
                add(User.AVATAR_SLEEPY);
            }}.contains(value)) {
                setError(name, MESSAGE_FIELD_INCORRECT, errors);
                return false;
            }
        }
        return true;
    }

    public static boolean isNickname(String name, Object model, Errors errors) {
        if (isRequired(name, model, errors)) {
            var value = findValue(name, model);
            if (Objects.requireNonNull(value).length() < 2) {
                setError(name, MESSAGE_FIELD_MIN_LENGTH, errors, 2);
                return false;
            } else if (value.length() > 30) {
                setError(name, MESSAGE_FIELD_MAX_LENGTH, errors, 30);
                return false;
            }
        }
        return true;
    }

    public static boolean isPassword(String name, Object model, Errors errors) {
        if (isRequired(name, model, errors)) {
            var value = findValue(name, model);
            if (Objects.requireNonNull(value).length() < 5) {
                setError(name, MESSAGE_FIELD_MIN_LENGTH, errors, 5);
                return false;
            } else if (value.length() > 30) {
                setError(name, MESSAGE_FIELD_MAX_LENGTH, errors, 30);
                return false;
            } else if (value.contains(" ")) {
                setError(name, MESSAGE_FIELD_INCORRECT_SPACES, errors);
                return false;
            }
        }
        return true;
    }
}
