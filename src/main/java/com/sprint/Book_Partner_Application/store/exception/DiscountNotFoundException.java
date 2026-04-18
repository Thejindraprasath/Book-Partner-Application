package com.sprint.Book_Partner_Application.store.exception;

import com.sprint.Book_Partner_Application.exception.ResourceNotFoundException;

public class DiscountNotFoundException extends ResourceNotFoundException {

    public DiscountNotFoundException(Long discountId) {
        super("Discount", "discountId", String.valueOf(discountId));
    }
    public DiscountNotFoundException(String discountType) {
        super("Discount", "discountType", discountType);
    }
}
