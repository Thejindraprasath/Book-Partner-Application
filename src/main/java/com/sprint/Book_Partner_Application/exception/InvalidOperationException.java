package com.sprint.Book_Partner_Application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when an operation is syntactically valid but logically impossible
 * given the current state of the data.
 *
 * HTTP 422 Unprocessable Entity
 *
 * Usage examples:
 *   throw new InvalidOperationException("Date range 'from' (2024-06-01) must be before 'to' (2024-01-01)");
 *   throw new InvalidOperationException("Royalty range [5001 - 3000] is invalid — lorange must be less than hirange");
 *   throw new InvalidOperationException("Royalty range [3000 - 8000] overlaps with existing range [5001 - 10000] for title 'BU1032'");
 *   throw new InvalidOperationException("Cannot associate Author '409-56-7008' with Title 'BU1032' — already linked");
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {
        super(message);
    }
}