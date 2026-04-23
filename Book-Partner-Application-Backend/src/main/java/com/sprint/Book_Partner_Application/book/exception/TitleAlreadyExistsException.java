package com.sprint.Book_Partner_Application.book.exception;

import com.sprint.Book_Partner_Application.exception.DuplicateResourceException;

/**
 * Thrown when trying to create a title that already exists.
 */
public class TitleAlreadyExistsException extends DuplicateResourceException {

    public TitleAlreadyExistsException(String titleId) {
        super("Title", "titleId", titleId);
    }
}