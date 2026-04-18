package com.sprint.Book_Partner_Application.author.exception;

import com.sprint.Book_Partner_Application.exception.ResourceNotFoundException;

public class AuthorNotFoundException extends ResourceNotFoundException {

    public AuthorNotFoundException(String auId) {
        super("Author", "auId", auId);
    }
}