package com.sprint.Book_Partner_Application.book.exception;

import com.sprint.Book_Partner_Application.exception.BusinessValidationException;

import java.util.Set;

public class InvalidTitleTypeException extends BusinessValidationException {

    public static final Set<String> VALID_TYPES = Set.of(
            "business",
            "mod_cook",
            "trad_cook",
            "popular_comp",
            "psychology",
            "UNDECIDED"
    );

    public InvalidTitleTypeException(String type) {
        super("type", "invalid value '" + type + "'. Allowed: " + VALID_TYPES);
    }
}