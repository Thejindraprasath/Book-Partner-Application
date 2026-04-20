package com.sprint.Book_Partner_Application.book.exception;

import com.sprint.Book_Partner_Application.exception.InvalidOperationException;

public class RoySchedRangeOverlapException extends InvalidOperationException {

    public RoySchedRangeOverlapException(String titleId, int lorange, int hirange) {
        super("Royalty range [" + lorange + " - " + hirange +
                "] overlaps with an existing range for title '" + titleId + "'");
    }
}