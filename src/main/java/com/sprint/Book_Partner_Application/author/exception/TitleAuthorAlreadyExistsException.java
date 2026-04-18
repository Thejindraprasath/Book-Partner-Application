package com.sprint.Book_Partner_Application.author.exception;

import com.sprint.Book_Partner_Application.exception.DuplicateResourceException;

public class TitleAuthorAlreadyExistsException extends DuplicateResourceException {

    public TitleAuthorAlreadyExistsException(String auId, String titleId) {
        super("Author-Title association", "auId & titleId", auId + ", " + titleId);
    }
}