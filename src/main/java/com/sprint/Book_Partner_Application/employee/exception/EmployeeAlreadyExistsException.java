package com.sprint.Book_Partner_Application.employee.exception;

import com.sprint.Book_Partner_Application.exception.DuplicateResourceException;

public class EmployeeAlreadyExistsException extends DuplicateResourceException {
    public EmployeeAlreadyExistsException(String empId) {
        super("Employee", "empId", empId);
    }
}