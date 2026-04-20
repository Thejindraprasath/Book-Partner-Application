package com.sprint.Book_Partner_Application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when attempting to create a resource that already exists.
 * Used across ALL modules — authors, titles, publishers, employees, stores, sales.
 *
 * HTTP 409 Conflict
 *
 * Usage examples:
 *   throw new DuplicateResourceException("Author", "auId", "409-56-7008");
 *   throw new DuplicateResourceException("Store", "storId", "7066");
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(resourceName + " already exists with " + fieldName + " = '" + fieldValue + "'");
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() { return resourceName; }
    public String getFieldName()    { return fieldName; }
    public Object getFieldValue()   { return fieldValue; }
}