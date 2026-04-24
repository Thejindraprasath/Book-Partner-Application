package com.sprint.Book_Partner_Application.book.exception;

import com.sprint.Book_Partner_Application.exception.InvalidOperationException;

/**
 * Thrown when a royalty schedule range overlaps with an existing range.
 */
public class RoySchedRangeOverlapException extends InvalidOperationException {

    public RoySchedRangeOverlapException(String titleId, Integer lorange, Integer hirange) {
        super("Royalty range [" + lorange + " - " + hirange +
                "] overlaps with an existing range for titleId '" + titleId + "'");
    }
}