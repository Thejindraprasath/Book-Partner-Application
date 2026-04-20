package com.sprint.Book_Partner_Application.book.exception;

import com.sprint.Book_Partner_Application.exception.ResourceInUseException;

public class TitleHasActiveAuthorsException extends ResourceInUseException {

    public TitleHasActiveAuthorsException(String titleId, int count) {
        super("Title", titleId, count + " author association(s). Remove those associations first");
    }
}