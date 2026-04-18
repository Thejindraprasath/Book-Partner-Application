package com.sprint.Book_Partner_Application.publisher.exception;

import com.sprint.Book_Partner_Application.exception.ResourceInUseException;

public class PublisherHasActiveTitlesException extends ResourceInUseException {
    public PublisherHasActiveTitlesException(String pubId, long count) {
        super("Publisher", pubId, count + " title(s) exist. Resolve dependencies first");
    }
}