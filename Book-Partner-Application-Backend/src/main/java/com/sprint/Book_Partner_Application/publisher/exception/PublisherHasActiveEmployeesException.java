package com.sprint.Book_Partner_Application.publisher.exception;

import com.sprint.Book_Partner_Application.exception.ResourceInUseException;

public class PublisherHasActiveEmployeesException extends ResourceInUseException {
    public PublisherHasActiveEmployeesException(String pubId, long count) {
        super("Publisher", pubId, count + " employee(s) exist. Resolve dependencies first");
    }
}