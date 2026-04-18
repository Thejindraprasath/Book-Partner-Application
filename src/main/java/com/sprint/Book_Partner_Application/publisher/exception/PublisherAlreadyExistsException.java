package com.sprint.Book_Partner_Application.publisher.exception;

import com.sprint.Book_Partner_Application.exception.DuplicateResourceException;

public class PublisherAlreadyExistsException extends DuplicateResourceException {
    public PublisherAlreadyExistsException(String pubId) {
        super("Publisher", "pubId", pubId);
    }
}