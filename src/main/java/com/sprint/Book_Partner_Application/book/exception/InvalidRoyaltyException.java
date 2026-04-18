package com.sprint.Book_Partner_Application.book.exception;

import com.sprint.Book_Partner_Application.exception.BusinessValidationException;

public class InvalidRoyaltyException extends BusinessValidationException {

    public InvalidRoyaltyException(int royalty) {
        super("royalty", "must be between 0 and 100, got: " + royalty);
    }
}
