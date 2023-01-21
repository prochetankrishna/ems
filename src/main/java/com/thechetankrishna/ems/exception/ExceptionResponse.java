package com.thechetankrishna.ems.exception;

public class ExceptionResponse {
    private String errorMessage;
    private String errorCode;
    private String errorTimestamp;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorTimestamp() {
        return errorTimestamp;
    }

    public void setErrorTimestamp(String errorTimestamp) {
        this.errorTimestamp = errorTimestamp;
    }
}
