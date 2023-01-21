package com.thechetankrishna.ems.exception;

public class NoEmployeeExistsException extends RuntimeException{

    public NoEmployeeExistsException(String errorMessage) {
        super(errorMessage);
    }
}
