package com.sprint.Book_Partner_Application.book.exception;

import com.sprint.Book_Partner_Application.exception.BusinessValidationException;

public class InvalidPriceException extends BusinessValidationException {

    public InvalidPriceException(Double price) {
        super("price", "must be greater than 0, got: " + price);
    }

    public InvalidPriceException(String message) {
        super("price", message);
    }
}