package com.sprint.Book_Partner_Application.sales.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class SaleNotFoundException extends RuntimeException {
    public SaleNotFoundException(String storId, String ordNum, String titleId) {
        super("Sale not found with storId='" + storId +
                "', ordNum='" + ordNum +
                "', titleId='" + titleId + "'");
    }
}
