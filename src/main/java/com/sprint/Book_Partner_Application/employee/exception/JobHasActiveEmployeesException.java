package com.sprint.Book_Partner_Application.employee.exception;

import com.sprint.Book_Partner_Application.exception.ResourceInUseException;

public class JobHasActiveEmployeesException extends ResourceInUseException {
    public JobHasActiveEmployeesException(Short jobId, int count) {
        super("Job", String.valueOf(jobId),
                count + " active employee(s) assigned. Reassign them first");
    }
}