package com.sprint.Book_Partner_Application.employee.exception;

import com.sprint.Book_Partner_Application.exception.BusinessValidationException;

public class InvalidJobLevelRangeException extends BusinessValidationException {
    public InvalidJobLevelRangeException(int minLvl, int maxLvl) {
        super("minLvl / maxLvl", "minLvl (" + minLvl + ") must be less than maxLvl (" + maxLvl + ")");
    }
}
