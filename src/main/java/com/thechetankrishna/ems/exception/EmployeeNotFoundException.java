package com.thechetankrishna.ems.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
