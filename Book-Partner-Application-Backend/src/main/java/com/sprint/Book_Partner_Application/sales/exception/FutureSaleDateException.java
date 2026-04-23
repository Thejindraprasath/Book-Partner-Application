package com.sprint.Book_Partner_Application.sales.exception;

import com.sprint.Book_Partner_Application.exception.BusinessValidationException;

import java.time.LocalDateTime;

public class FutureSaleDateException extends BusinessValidationException {
    public FutureSaleDateException(LocalDateTime ordDate) {
        super("ordDate", "cannot be in the future, got: " + ordDate);
    }
}
