package com.sprint.Book_Partner_Application.book.exception;

import com.sprint.Book_Partner_Application.exception.DuplicateResourceException;

public class TitleAlreadyExistsException extends DuplicateResourceException {

    public TitleAlreadyExistsException(String titleId) {
        super("Title", "titleId", titleId);
    }
}