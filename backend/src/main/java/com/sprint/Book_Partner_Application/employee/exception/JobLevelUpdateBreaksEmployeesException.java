package com.sprint.Book_Partner_Application.employee.exception;

import com.sprint.Book_Partner_Application.exception.BusinessValidationException;

public class JobLevelUpdateBreaksEmployeesException extends BusinessValidationException {
    public JobLevelUpdateBreaksEmployeesException(String empId, int jobLvl, int minLvl, int maxLvl) {
        super("minLvl / maxLvl", "Employee '" + empId + "' has jobLvl=" + jobLvl
                + " which falls outside the new range [" + minLvl + " - " + maxLvl + "]. Update that employee's level first");
    }
}
