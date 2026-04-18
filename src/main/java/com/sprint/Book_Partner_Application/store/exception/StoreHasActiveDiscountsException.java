package com.sprint.Book_Partner_Application.store.exception;

import com.sprint.Book_Partner_Application.exception.ResourceInUseException;

public class StoreHasActiveDiscountsException extends ResourceInUseException {
    public StoreHasActiveDiscountsException(String storId, int count) {
        super("Store", storId, count + " existing discount(s). Remove them first");
    }
}

