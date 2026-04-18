package com.sprint.Book_Partner_Application.store.exception;

import com.sprint.Book_Partner_Application.exception.BusinessValidationException;

public class InvalidStateCodeException extends BusinessValidationException {
    public InvalidStateCodeException(String state) {
        super("state", "must be a 2-character code (received: '" + state + "')");
    }
}
