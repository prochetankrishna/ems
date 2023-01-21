package com.thechetankrishna.ems.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

@ControllerAdvice
public class EmployeeExceptionHandler extends ResponseEntityExceptionHandler {

    private final String TIME_STAMP_FORMAT = "yyyy.MM.dd'T'HH.mm.ss.SSSXXX";

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEmployeeNotFoundException(
            EmployeeNotFoundException employeeNotFoundException) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorCode("404");
        exceptionResponse.setErrorMessage(employeeNotFoundException.getMessage());
        exceptionResponse.setErrorTimestamp(new SimpleDateFormat(TIME_STAMP_FORMAT)
                .format(new Date()));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoEmployeeExistsException.class)
    public ResponseEntity<ExceptionResponse> handleNoEmployeeExistsException(
            NoEmployeeExistsException noEmployeeExistsException) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorCode("404");
        exceptionResponse.setErrorMessage(noEmployeeExistsException.getMessage());
        exceptionResponse.setErrorTimestamp(new SimpleDateFormat(TIME_STAMP_FORMAT)
                .format(new Date()));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidEmployeeRequestException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidEmployeeRequestException(
            InvalidEmployeeRequestException invalidEmployeeRequestException) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorCode("400");
        exceptionResponse.setErrorMessage(invalidEmployeeRequestException.getMessage());
        exceptionResponse.setErrorTimestamp(new SimpleDateFormat(TIME_STAMP_FORMAT)
                .format(new Date()));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
