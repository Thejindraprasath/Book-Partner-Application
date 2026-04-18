package com.sprint.Book_Partner_Application.book.exception;

import com.sprint.Book_Partner_Application.exception.ResourceInUseException;

public class TitleHasActiveSalesException extends ResourceInUseException {

    public TitleHasActiveSalesException(String titleId, int count) {
        super("Title", titleId, count + " sales record(s). Remove those sales first");
    }
}