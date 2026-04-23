package com.sprint.Book_Partner_Application.store.exception;

import com.sprint.Book_Partner_Application.exception.InvalidOperationException;

public class InvalidDiscountQtyRangeException extends InvalidOperationException {
    public InvalidDiscountQtyRangeException(short lowqty, short highqty) {
        super("Invalid quantity range: lowqty (" + lowqty +
                ") must be less than highqty (" + highqty + ")");
    }
}

