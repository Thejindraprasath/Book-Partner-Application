package com.sprint.Book_Partner_Application.employee.exception;

import com.sprint.Book_Partner_Application.exception.ResourceNotFoundException;

public class JobNotFoundException extends ResourceNotFoundException {
    public JobNotFoundException(Short jobId) {
        super("Job", "jobId", String.valueOf(jobId));
    }
}
