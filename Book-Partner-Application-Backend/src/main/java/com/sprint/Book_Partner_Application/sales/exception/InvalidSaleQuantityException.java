package com.sprint.Book_Partner_Application.sales.exception;

import com.sprint.Book_Partner_Application.exception.BusinessValidationException;

public class InvalidSaleQuantityException extends BusinessValidationException {
    public InvalidSaleQuantityException(Short qty) {
        super("qty", "must be at least 1, got: " + String.valueOf(qty));
    }
}