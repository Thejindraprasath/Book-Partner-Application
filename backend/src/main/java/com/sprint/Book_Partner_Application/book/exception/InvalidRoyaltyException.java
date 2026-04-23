package com.sprint.Book_Partner_Application.book.exception;

import com.sprint.Book_Partner_Application.exception.BusinessValidationException;

/**
 * Thrown when the royalty value is outside the valid range.
 */
public class InvalidRoyaltyException extends BusinessValidationException {

    public InvalidRoyaltyException(Integer royalty) {
        super("royalty", "must be between 0 and 100, but got: " + royalty);
    }

    public InvalidRoyaltyException(String message) {
        super("royalty", message);
    }
}