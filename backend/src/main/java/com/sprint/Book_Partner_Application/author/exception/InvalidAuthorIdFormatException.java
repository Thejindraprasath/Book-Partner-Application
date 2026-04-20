package com.sprint.Book_Partner_Application.author.exception;

import com.sprint.Book_Partner_Application.exception.BusinessValidationException;

public class InvalidAuthorIdFormatException extends BusinessValidationException {

    public InvalidAuthorIdFormatException(String auId) {
        super("auId", "Invalid format '" + auId + "'. Expected ###-##-#### (e.g. 409-56-7008)");
    }
}