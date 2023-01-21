package com.thechetankrishna.ems.exception;

public class InvalidEmployeeRequestException extends RuntimeException {

    public InvalidEmployeeRequestException(String errorMessage) {
        super(errorMessage);
    }
}
