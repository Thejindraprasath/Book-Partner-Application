package com.sprint.Book_Partner_Application.author.exception;

import com.sprint.Book_Partner_Application.exception.DuplicateResourceException;

public class AuthorAlreadyExistsException extends DuplicateResourceException {

    public AuthorAlreadyExistsException(String auId) {
        super("Author", "auId", auId);
    }
}