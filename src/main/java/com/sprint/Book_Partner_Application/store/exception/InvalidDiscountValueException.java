package com.sprint.Book_Partner_Application.store.exception;

import com.sprint.Book_Partner_Application.exception.BusinessValidationException;

public class InvalidDiscountValueException extends BusinessValidationException {
    public InvalidDiscountValueException(Object value) {
        super("discount", "must be between 0.00 and 100.00 (received: " + value + ")");
    }
}