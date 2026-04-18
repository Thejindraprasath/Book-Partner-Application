package com.sprint.Book_Partner_Application.store.exception;

public class DiscountAlreadyExistsException extends RuntimeException {
    public DiscountAlreadyExistsException(String discountType, String storId) {
        super("Discount already exists for store '" + storId +
                "' with discountType='" + discountType + "'");
    }
}