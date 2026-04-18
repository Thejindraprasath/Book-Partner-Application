package com.sprint.Book_Partner_Application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when business/domain rules are violated that cannot be expressed
 * through Bean Validation annotations (@NotNull, @Pattern, etc.).
 *
 * HTTP 400 Bad Request
 *
 * Usage examples:
 *   throw new BusinessValidationException("Job level 300 exceeds the maximum allowed level of 250 for job 'Editor'");
 *   throw new BusinessValidationException("Royalty percentage must be between 0 and 100, got: 150");
 *   throw new BusinessValidationException("Author order (au_ord) must be unique per title");
 *   throw new BusinessValidationException("Price must be greater than 0");
 *   throw new BusinessValidationException("Discount value 105.00 is invalid — must be between 0.00 and 100.00");
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessValidationException extends RuntimeException {

    public BusinessValidationException(String message) {
        super(message);
    }

    public BusinessValidationException(String field, String reason) {
        super("Validation failed for '" + field + "': " + reason);
    }
}