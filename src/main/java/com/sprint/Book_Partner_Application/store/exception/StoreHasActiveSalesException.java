package com.sprint.Book_Partner_Application.store.exception;

import com.sprint.Book_Partner_Application.exception.ResourceInUseException;

public class StoreHasActiveSalesException extends ResourceInUseException {
    public StoreHasActiveSalesException(String storId, int count) {
        super("Store", storId, count + " existing sale(s). Remove them first");
    }
}

