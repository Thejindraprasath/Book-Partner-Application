package com.sprint.Book_Partner_Application.sales.exception;

import com.sprint.Book_Partner_Application.exception.BusinessValidationException;

import java.util.Set;

public class InvalidPaytermsException extends BusinessValidationException {

    public static final Set<String> VALID_PAYTERMS =
            Set.of("Net 30", "Net 60", "ON invoice", "Net 90");

    public InvalidPaytermsException(String payterms) {
        super("payterms", "invalid value '" + payterms + "'. Allowed values: " + VALID_PAYTERMS);
    }
}
