package com.sprint.Book_Partner_Application.book.exception;

import com.sprint.Book_Partner_Application.exception.ResourceNotFoundException;

/**
 * Thrown when a royalty schedule is not found.
 */
public class RoySchedNotFoundException extends ResourceNotFoundException {

    public RoySchedNotFoundException(Long roySchedId) {
        super("RoySched", "roySchedId", roySchedId);
    }
}