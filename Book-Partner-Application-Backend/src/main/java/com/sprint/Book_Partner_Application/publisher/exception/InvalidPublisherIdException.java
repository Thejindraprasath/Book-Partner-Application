package com.sprint.Book_Partner_Application.publisher.exception;

import com.sprint.Book_Partner_Application.exception.BusinessValidationException;

public class InvalidPublisherIdException extends BusinessValidationException {
    public InvalidPublisherIdException(String pubId) {
        super("pubId", "invalid format (expected predefined ID or pattern 99XX, received: '" + pubId + "')");
    }
}