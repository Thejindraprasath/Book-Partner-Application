package com.sprint.Book_Partner_Application.book.exception;

import com.sprint.Book_Partner_Application.exception.ResourceInUseException;

/**
 * Thrown when a title cannot be deleted because it still has sales records.
 */
public class TitleHasActiveSalesException extends ResourceInUseException {

    public TitleHasActiveSalesException(String titleId, Integer count) {
        super("Title", titleId, count + " sales record(s) found. Remove those sales first");
    }
}