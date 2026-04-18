package com.sprint.Book_Partner_Application.book.exception;

import com.sprint.Book_Partner_Application.exception.InvalidOperationException;

public class InvalidRoySchedRangeException extends InvalidOperationException {

    public InvalidRoySchedRangeException(int lorange, int hirange) {
        super("lorange (" + lorange + ") must be less than hirange (" + hirange + ")");
    }
}
