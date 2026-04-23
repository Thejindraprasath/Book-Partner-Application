package com.sprint.Book_Partner_Application.book.exception;

import com.sprint.Book_Partner_Application.exception.ResourceInUseException;

/**
 * Thrown when a title cannot be deleted because it is still linked to authors.
 */
public class TitleHasActiveAuthorsException extends ResourceInUseException {

    public TitleHasActiveAuthorsException(String titleId, Integer count) {
        super("Title", titleId, count + " author association(s) found. Remove those associations first");
    }
}