package com.sprint.Book_Partner_Application.book.exception;

import com.sprint.Book_Partner_Application.exception.ResourceNotFoundException;

public class RoySchedNotFoundException extends ResourceNotFoundException {

    public RoySchedNotFoundException(Long roySchedId) {
        super("RoySched", "roySchedId", roySchedId);
    }
}