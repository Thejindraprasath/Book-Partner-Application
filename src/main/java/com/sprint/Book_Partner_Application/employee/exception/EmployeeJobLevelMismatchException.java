package com.sprint.Book_Partner_Application.employee.exception;

import com.sprint.Book_Partner_Application.exception.BusinessValidationException;

public class EmployeeJobLevelMismatchException extends BusinessValidationException {
    public EmployeeJobLevelMismatchException(int jobLvl, String jobDesc, int minLvl, int maxLvl) {
        super("jobLvl", "value " + jobLvl + " is outside the allowed range [" + minLvl + " - " + maxLvl + "] for job '" + jobDesc + "'");
    }
}