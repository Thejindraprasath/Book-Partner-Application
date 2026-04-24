package com.sprint.Book_Partner_Application.store.exception;

import com.sprint.Book_Partner_Application.exception.DuplicateResourceException;

public class StoreAlreadyExistsException extends DuplicateResourceException {
    public StoreAlreadyExistsException(String storId) {
        super("Store", "storId", storId);
    }
}