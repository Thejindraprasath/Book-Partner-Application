package com.sprint.Book_Partner_Application.store.exception;

import com.sprint.Book_Partner_Application.exception.BusinessValidationException;

public class InvalidZipCodeException extends BusinessValidationException {
    public InvalidZipCodeException(String zip) {
        super("zip", "must be exactly 5 digits (received: '" + zip + "')");
    }
}