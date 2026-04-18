package com.sprint.Book_Partner_Application.publisher.exception;

import com.sprint.Book_Partner_Application.exception.ResourceNotFoundException;

public class PublisherNotFoundException extends ResourceNotFoundException {
    public PublisherNotFoundException(String pubId) {
        super("Publisher", "pubId", pubId);
    }
}