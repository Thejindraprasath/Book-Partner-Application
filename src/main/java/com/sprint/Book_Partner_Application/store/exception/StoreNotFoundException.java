package com.sprint.Book_Partner_Application.store.exception;

import com.sprint.Book_Partner_Application.exception.ResourceNotFoundException;

public class StoreNotFoundException extends ResourceNotFoundException {
    public StoreNotFoundException(String storId) {
        super("Store", "storId", storId);
    }
}