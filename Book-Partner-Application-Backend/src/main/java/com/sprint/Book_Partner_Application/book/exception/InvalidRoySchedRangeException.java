package com.sprint.Book_Partner_Application.book.exception;

import com.sprint.Book_Partner_Application.exception.InvalidOperationException;

/**
 * Thrown when royalty schedule lower range is not less than higher range.
 */
public class InvalidRoySchedRangeException extends InvalidOperationException {

    public InvalidRoySchedRangeException(Integer lorange, Integer hirange) {
        super("Invalid royalty schedule range: lorange (" + lorange +
                ") must be less than hirange (" + hirange + ")");
    }
}