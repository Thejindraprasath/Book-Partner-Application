package com.sprint.Book_Partner_Application.book.exception;

import com.sprint.Book_Partner_Application.exception.ResourceNotFoundException;

/**
 * Thrown when a title is not found.
 */
public class TitleNotFoundException extends ResourceNotFoundException {

    public TitleNotFoundException(String titleId) {
        super("Title", "titleId", titleId);
    }
}