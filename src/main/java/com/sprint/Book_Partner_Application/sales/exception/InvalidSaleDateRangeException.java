package com.sprint.Book_Partner_Application.sales.exception;

import com.sprint.Book_Partner_Application.exception.InvalidOperationException;

import java.time.LocalDateTime;

public class InvalidSaleDateRangeException extends InvalidOperationException {

    public InvalidSaleDateRangeException(LocalDateTime from, LocalDateTime to) {
        super("Invalid date range: 'from' (" + from + ") must be before 'to' (" + to + ").");
    }

    public InvalidSaleDateRangeException(String reason) {
        super(reason);
    }
}