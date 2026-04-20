package com.sprint.Book_Partner_Application.author.exception;

import com.sprint.Book_Partner_Application.exception.ResourceInUseException;

public class AuthorHasActiveTitlesException extends ResourceInUseException {

    public AuthorHasActiveTitlesException(String auId) {
        super("Author", auId, "active titles");
    }
}