package com.sprint.Book_Partner_Application.sales.exception;

public class SaleAlreadyExistsException extends RuntimeException {
    public SaleAlreadyExistsException(String storId, String ordNum, String titleId) {
        super("Sale already exists with storId='" + storId +
                "', ordNum='" + ordNum +
                "', titleId='" + titleId + "'");
    }
}

