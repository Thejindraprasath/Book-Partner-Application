package com.sprint.Book_Partner_Application.author.exception;

import com.sprint.Book_Partner_Application.exception.ResourceNotFoundException;

public class TitleAuthorNotFoundException extends ResourceNotFoundException {

    public TitleAuthorNotFoundException(String auId, String titleId) {
        super("Author-Title association", "auId & titleId", auId + ", " + titleId);
    }
}